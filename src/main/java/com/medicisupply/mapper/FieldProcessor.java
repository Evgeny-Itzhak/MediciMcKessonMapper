package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;

import java.util.Map;

public class FieldProcessor {

    public static String process(CsvField field, ProductRow row, Map<CsvField, String> mapping) {
        switch (field) {
            case COMMAND:
                return field.getDefaultValue();
            case HANDLE:
            case TITLE:
            case BODYHTML:
            case VENDOR:
            case TESTCOLUMN:
                String sourceColumn = mapping.get(field);
                if (sourceColumn != null) {
                    String value = row.getField(sourceColumn);
                    return (value == null || value.isEmpty()) && field.getDefaultValue() != null
                            ? field.getDefaultValue()
                            : value;
                }
                return "";
            default:
                return "";
        }
    }
}