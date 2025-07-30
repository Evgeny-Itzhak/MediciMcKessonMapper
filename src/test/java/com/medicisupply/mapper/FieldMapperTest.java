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
        assertEquals("Supply Manager Category", map.get(CsvField.CUSTOM_COLLECTIONS));
        assertEquals("Shipping Weight", map.get(CsvField.VARIANT_WEIGHT));
        assertEquals("Shipping Width", map.get(CsvField.SHIPPING_WIDTH));
        assertEquals("Shipping Height", map.get(CsvField.SHIPPING_HEIGHT));
        assertEquals("Shipping Depth", map.get(CsvField.SHIPPING_DEPTH));
        assertEquals("Shipping Weight", map.get(CsvField.SHIPPING_WEIGHT));
    }

    @Test
    void testAllFieldsHaveSourceMapping() {
        Map<CsvField, String> map = FieldMapper.getFieldMappings();

        for (CsvField field : map.keySet()) {
            assertNotNull(map.get(field), "Mapping for field " + field + " should not be null");
        }
    }
}