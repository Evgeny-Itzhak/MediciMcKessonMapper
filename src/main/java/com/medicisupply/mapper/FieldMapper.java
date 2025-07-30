package com.medicisupply.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

    public static Map<CsvField, String> getFieldMappings() {
        Map<CsvField, String> map = new LinkedHashMap<>();

        map.put(CsvField.HANDLE, ExcelField.RETAIL_DESCRIPTION.getHeader());
        map.put(CsvField.COMMAND, ExcelField.COMMAND.getHeader());
        map.put(CsvField.TITLE, ExcelField.RETAIL_DESCRIPTION.getHeader());
        map.put(CsvField.BODY_HTML, ExcelField.RETAIL_FEATURES_AND_BENEFITS.getHeader());
        map.put(CsvField.VENDOR, ExcelField.BRAND_OR_SERIES.getHeader());
        map.put(CsvField.TYPE, ExcelField.APPLICATION.getHeader());
        map.put(CsvField.TAGS_COMMAND, ExcelField.APPLICATION.getHeader());
        map.put(CsvField.TAGS, ExcelField.SUPPLY_MANAGER_CATEGORY.getHeader());
        map.put(CsvField.STATUS, ExcelField.APPLICATION.getHeader());
        return map;
    }
}