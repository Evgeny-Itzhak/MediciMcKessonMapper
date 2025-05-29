package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldProcessorTest {

    @Test
    void testCommandFieldReturnsMerge() {
        ProductRow row = new ProductRow(); // not used
        Map<CsvField, String> mapping = FieldMapper.getFieldMappings();

        String result = FieldProcessor.process(CsvField.COMMAND, row, mapping);
        assertEquals("MERGE", result);
    }

    @Test
    void testHandleReadsMappedField() {
        ProductRow row = new ProductRow();
        row.setField("Retail Description", "Example Product");

        Map<CsvField, String> mapping = FieldMapper.getFieldMappings();
        String result = FieldProcessor.process(CsvField.HANDLE, row, mapping);

        assertEquals("Example Product", result);
    }

    @Test
    void testDefaultValueFallbackWhenNull() {
        ProductRow row = new ProductRow(); // No fields set

        Map<CsvField, String> mapping = FieldMapper.getFieldMappings();
        String result = FieldProcessor.process(CsvField.TESTCOLUMN, row, mapping);

        assertEquals("", result); // No default value defined
    }
}