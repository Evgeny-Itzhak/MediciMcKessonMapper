package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldProcessorTest {

    @Test
    void testHandleMapping() {
        ProductRow row = new ProductRow();
        row.setField("E1 SKU", "123-ABC");

        String result = FieldProcessor.process(CsvField.HANDLE, row);
        assertEquals("123-ABC", result);
    }

    @Test
    void testCommandDefaultValue() {
        ProductRow row = new ProductRow();
        String result = FieldProcessor.process(CsvField.COMMAND, row);
        assertEquals("MERGE", result);
    }

    @Test
    void testFallbackToDefaultValue() {
        ProductRow row = new ProductRow(); // No field set
        CsvField fieldWithDefault = CsvField.COMMAND;
        String result = FieldProcessor.process(fieldWithDefault, row);
        assertEquals("MERGE", result);
    }
}