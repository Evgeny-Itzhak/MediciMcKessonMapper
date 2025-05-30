package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldProcessorTest {

    @Test
    void testBuildHandle_withAllParts() {
        // Prepare a ProductRow with retailDesc, itemNo and manufacturerNumber
        ProductRow row = new ProductRow();
        row.setField("Retail Description", "Active Compression Pantyhose");
        row.setField("McK Item No", "824269");
        row.setField("Manufacturer Number", "H3703");

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);

        // Act
        String handle = FieldProcessor.process(CsvField.HANDLE, row, mapping);

        // Assert
        assertEquals(
                "Active Compression Pantyhose MSC-MCK-824269 H3703",
                handle
        );
    }

    @Test
    void testBuildHandle_withoutManufacturerNumber() {
        // When manufacturerNumber is missing or empty, handle should not end with extra space
        ProductRow row = new ProductRow();
        row.setField("Retail Description", "ProductX");
        row.setField("McK Item No", "12345");
        // manufacturerNumber not set: null or empty

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);

        String handle = FieldProcessor.process(CsvField.HANDLE, row, mapping);

        assertEquals(
                "ProductX MSC-MCK-12345",
                handle
        );
    }

    @Test
    void testProcessStandardField_withValue() {
        ProductRow row = new ProductRow();
        row.setField("Title", "MyTitle");

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        mapping.put(CsvField.TITLE, "Title");

        String title = FieldProcessor.process(CsvField.TITLE, row, mapping);

        assertEquals("MyTitle", title);
    }

    @Test
    void testProcessStandardField_withDefaultValue() {
        // If source cell is empty and field has defaultValue, it should return defaultValue
        ProductRow row = new ProductRow();
        // no Title in row → getField returns null

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        mapping.put(CsvField.TITLE, "Title");

        // Simulate CsvField.TITLE.getDefaultValue() != null
        String defaultVal = CsvField.TITLE.getDefaultValue();
        String result = FieldProcessor.process(CsvField.TITLE, row, mapping);

        assertEquals(defaultVal != null ? defaultVal : "", result);
    }

    @Test
    void testProcessCommandField_usesDefault() {
        // COMMAND is always produced via getDefaultValue()
        ProductRow row = new ProductRow();
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);

        String cmd = FieldProcessor.process(CsvField.COMMAND, row, mapping);

        assertEquals(CsvField.COMMAND.getDefaultValue(), cmd);
    }

    @Test
    void testUnknownField_returnsEmpty() {
        ProductRow row = new ProductRow();
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);

        // Use a CsvField not explicitly handled (if you have any extra)
        String out = FieldProcessor.process(null, row, mapping);
        assertEquals("", out);
    }
}