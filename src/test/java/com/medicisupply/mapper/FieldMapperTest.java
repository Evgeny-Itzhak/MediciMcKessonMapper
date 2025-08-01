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
        assertEquals("Dimension UOM", map.get(CsvField.DIMENSION_UOM));
        assertEquals("UPC", map.get(CsvField.VARIANT_BARCODE));
        assertEquals("Primary Image", map.get(CsvField.VARIANT_IMAGE));
        assertEquals("Size", map.get(CsvField.OPTION1_NAME));
        assertEquals("E1 SKU", map.get(CsvField.OPTION1_VALUE));
    }

    @Test
    void testAllFieldsHaveSourceMapping() {
        Map<CsvField, String> map = FieldMapper.getFieldMappings();

        for (CsvField field : map.keySet()) {
            assertNotNull(map.get(field), "Mapping for field " + field + " should not be null");
        }
    }
}