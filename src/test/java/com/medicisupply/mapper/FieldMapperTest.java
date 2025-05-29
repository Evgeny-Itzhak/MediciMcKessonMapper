package com.medicisupply.mapper;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldMapperTest {

    @Test
    void testFieldMappingContainsExpectedFields() {
        Map<String, CsvField> mappings = FieldMapper.getFieldMappings();
        assertTrue(mappings.containsKey("E1 SKU"));
        assertEquals(CsvField.HANDLE, mappings.get("E1 SKU"));
    }

    @Test
    void testCommandFieldIncluded() {
        Map<String, CsvField> mappings = FieldMapper.getFieldMappings();
        assertTrue(mappings.containsValue(CsvField.COMMAND));
    }
}