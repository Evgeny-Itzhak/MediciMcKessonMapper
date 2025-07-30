package com.medicisupply.mapper;

import com.medicisupply.context.AppContext;
import lombok.extern.log4j.Log4j2;

import com.medicisupply.model.ProductRow;

import java.util.*;

/**
 * Processes CSV fields based on the ProductRow data and mapping.
 */
@Log4j2
public class FieldProcessor {

    /**
     * Converts a CsvField for the given ProductRow into the correct CSV value.
     */
    public static String process(CsvField field, ProductRow row, Map<CsvField, String> mapping) {
        // Return empty for null field to avoid NullPointerException
        if (field == null) {
            return "";
        }

        switch (field) {
            case HANDLE:
                return buildHandle(row);

            case COMMAND:
            case TAGS_COMMAND:
            case STATUS:
                return field.getDefaultValue();

            case TITLE:
            case BODY_HTML:
            case VENDOR:
            case TYPE:
                return processStandardField(field, row, mapping);

            case TAGS:
                return buildTags(row, mapping);

            case IMAGE_SRC:
                return buildImageSrc(row);

            default:
                // Return empty string if no mapping defined
                return "";
        }
    }

    /**
     * Builds the HANDLE value using the template:
     * {Retail Description} MSC-MCK-{McK Item No} {Manufacturer Number}
     * Manufacturer Number is appended only if present.
     */
    private static String buildHandle(ProductRow row) {
        String retailDesc = Objects.toString(row.getField(ExcelField.RETAIL_DESCRIPTION.getHeader()), "");
        String mckItemNo = Objects.toString(row.getField(ExcelField.MCK_ITEM_NO.getHeader()), "");
        String manufacturerNumber = Objects.toString(row.getField(ExcelField.MANUFACTURER_NUMBER.getHeader()), "");

        List<String> parts = new ArrayList<>();
        if (!retailDesc.isEmpty()) {
            parts.add(retailDesc);
        }
        // Add fixed prefix with McK item number
        parts.add("MSC-MCK-" + mckItemNo);
        if (!manufacturerNumber.isEmpty()) {
            parts.add(manufacturerNumber);
        }

        return String.join(" ", parts);
    }

    /**
     * Builds the TAGS value using the template:
     * __Mckesson-data,__Mckesson-upload-{datePart},__Mckesson-{monthName}-Sheet-1,{SUPPLY_MANAGER_CATEGORY}
     */
    private static String buildTags(ProductRow row, Map<CsvField, String> mapping) {
        // 1) get the raw category value from Excel
        String supplyManagerCategoryColumn = mapping.get(CsvField.TAGS);
        String supplyManagerCategoryValue = Objects.toString(row.getField(supplyManagerCategoryColumn), "");

        // 2) try to get pre-parsed date/month from context
        String datePart = AppContext.getDatePart();
        String month = AppContext.getMonthName();

        // 3) fallback: only category if no date info
        if (datePart == null || month == null) {
            log.warn("Date info is missing in AppContext; using SUPPLY_MANAGER_CATEGORY only for TAGS");
            return supplyManagerCategoryValue;
        }

        // 4) assemble full TAGS string
        return String.format(
                "__Mckesson-data,__Mckesson-upload-%s,__Mckesson-%s-Sheet-1,%s",
                datePart, month, supplyManagerCategoryValue
        );
    }


    /**
     * Processes standard fields (TITLE, BODY_HTML, etc.) based on mapping.
     * 
     * @param field The CSV field to process
     * @param row The product row containing the data
     * @param mapping The mapping from CSV fields to Excel field headers
     * @return The processed field value, or default value, or empty string
     */
    public static String processStandardField(CsvField field, ProductRow row, Map<CsvField, String> mapping) {
        // Handle null field
        if (field == null) {
            log.warn("Null field passed to processStandardField");
            return "";
        }
        
        // Handle null row
        if (row == null) {
            log.warn("Null row passed to processStandardField for field: {}", field);
            return field.getDefaultValue() != null ? field.getDefaultValue() : "";
        }
        
        // Handle null mapping
        if (mapping == null) {
            log.warn("Null mapping passed to processStandardField for field: {}", field);
            return field.getDefaultValue() != null ? field.getDefaultValue() : "";
        }
        
        String sourceColumn = mapping.get(field);
        if (sourceColumn != null) {
            String value = row.getField(sourceColumn);
            if ((value == null || value.isEmpty()) && field.getDefaultValue() != null) {
                return field.getDefaultValue();
            }
            return value != null ? value : "";
        }
        
        log.warn("No mapping found for field: {}", field);
        return field.getDefaultValue() != null ? field.getDefaultValue() : "";
    }
    
    /**
     * Builds the IMAGE_SRC value by combining multiple image fields with semicolons.
     * Fields: Primary Image, Image Reference Number 2-10
     */
    private static String buildImageSrc(ProductRow row) {
        List<String> imageFields = new ArrayList<>();
        
        // Add Primary Image
        String primaryImage = Objects.toString(row.getField(ExcelField.PRIMARY_IMAGE.getHeader()), "");
        imageFields.add(primaryImage);
        
        // Add Image Reference Number 2-10
        for (int i = 2; i <= 10; i++) {
            String fieldName = "Image Reference Number " + i;
            String imageValue = Objects.toString(row.getField(fieldName), "");
            imageFields.add(imageValue);
        }
        
        // Join all fields with semicolons
        return String.join(";", imageFields);
    }
}