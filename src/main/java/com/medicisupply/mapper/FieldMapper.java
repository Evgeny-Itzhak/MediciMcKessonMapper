package com.medicisupply.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

    public static Map<CsvField, String> getFieldMappings() {
        Map<CsvField, String> map = new LinkedHashMap<>();

        map.put(CsvField.HANDLE, "Retail Description");
        map.put(CsvField.COMMAND, "Command");
        map.put(CsvField.TITLE, "Retail Description");
        map.put(CsvField.BODYHTML, "Body HTML");
        map.put(CsvField.VENDOR, "Brand or Series");
        map.put(CsvField.TESTCOLUMN, "TestColumn");

        return map;
    }
}