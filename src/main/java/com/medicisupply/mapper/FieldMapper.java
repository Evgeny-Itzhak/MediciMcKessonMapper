
package com.medicisupply.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {
    public static Map<String, CsvField> getFieldMappings() {
        Map<String, CsvField> map = new LinkedHashMap<>();
        map.put("E1 SKU", CsvField.HANDLE);
        map.put("Command", CsvField.COMMAND);
        map.put("Body HTML", CsvField.BODYHTML);
        map.put("Retail Description", CsvField.TITLE);
        map.put("Brand or Series", CsvField.VENDOR);
        map.put("TestColumn", CsvField.TESTCOLUMN);
        // Add more mappings as needed
        return map;
    }
}
