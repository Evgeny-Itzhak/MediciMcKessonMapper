package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Processes CSV fields based on the ProductRow data and mapping.
 */
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
                return field.getDefaultValue();

            case TITLE:
            case BODY_HTML:
            case VENDOR:
            case TESTCOLUMN:
                return processStandardField(field, row, mapping);

            default:
                // Return empty string if no mapping defined
                return "";
        }
    }

    /**
     * Builds the HANDLE value using the template:
     *   {Retail Description} MSC-MCK-{McK Item No} {Manufacturer Number}
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
     * Processes standard fields (TITLE, BODY_HTML, etc.) based on mapping.
     */
    private static String processStandardField(CsvField field, ProductRow row, Map<CsvField, String> mapping) {
        String sourceColumn = mapping.get(field);
        if (sourceColumn != null) {
            String value = row.getField(sourceColumn);
            if ((value == null || value.isEmpty()) && field.getDefaultValue() != null) {
                return field.getDefaultValue();
            }
            return value;
        }
        return "";
    }
}