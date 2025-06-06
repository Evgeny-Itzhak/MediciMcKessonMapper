package com.medicisupply.mapper;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldMapperTest {

    @Test
    void testFieldMappingContainsExpectedFields() {
        Map<CsvField, String> map = FieldMapper.getFieldMappings();

        assertEquals("Retail Description", map.get(CsvField.HANDLE));
        assertEquals("Retail Description", map.get(CsvField.TITLE));
        assertEquals("Brand or Series", map.get(CsvField.VENDOR));
    }

    @Test
    void testAllFieldsHaveSourceMapping() {
        Map<CsvField, String> map = FieldMapper.getFieldMappings();

        for (CsvField field : map.keySet()) {
            assertNotNull(map.get(field), "Mapping for field " + field + " should not be null");
        }
    }
}