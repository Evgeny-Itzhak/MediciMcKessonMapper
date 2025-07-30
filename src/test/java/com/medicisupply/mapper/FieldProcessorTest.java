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
    
    @Test
    void testBuildImageSrc_combinesMultipleImages() {
        // Prepare a ProductRow with primary image and image reference numbers
        ProductRow row = new ProductRow();
        row.setField("Primary Image", "http://example.com/image1.jpg");
        row.setField("Image Reference Number 2", "http://example.com/image2.jpg");
        row.setField("Image Reference Number 3", "http://example.com/image3.jpg");
        // Leave some image references empty
        row.setField("Image Reference Number 5", "http://example.com/image5.jpg");
        
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        
        // Act
        String imageSrc = FieldProcessor.process(CsvField.IMAGE_SRC, row, mapping);
        
        // Assert
        // Should contain all images separated by semicolons, including empty ones
        assertEquals(
                "http://example.com/image1.jpg;http://example.com/image2.jpg;http://example.com/image3.jpg;;http://example.com/image5.jpg;;;;;",
                imageSrc
        );
    }
    
    @Test
    void testBuildImageSrc_withNoImages() {
        // Prepare a ProductRow with no images
        ProductRow row = new ProductRow();
        
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        
        // Act
        String imageSrc = FieldProcessor.process(CsvField.IMAGE_SRC, row, mapping);
        
        // Assert
        // Should contain 10 empty fields separated by semicolons
        assertEquals(";;;;;;;;;", imageSrc);
    }
    
    @Test
    void testProcessStandardField_withNullField() {
        ProductRow row = new ProductRow();
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        
        // Act
        String result = FieldProcessor.processStandardField(null, row, mapping);
        
        // Assert
        assertEquals("", result);
    }
    
    @Test
    void testProcessStandardField_withNullRow() {
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        
        // Act
        String result = FieldProcessor.processStandardField(CsvField.TITLE, null, mapping);
        
        // Assert
        String expectedDefault = CsvField.TITLE.getDefaultValue();
        assertEquals(expectedDefault != null ? expectedDefault : "", result);
    }
    
    @Test
    void testProcessStandardField_withNullMapping() {
        ProductRow row = new ProductRow();
        
        // Act
        String result = FieldProcessor.processStandardField(CsvField.COMMAND, row, null);
        
        // Assert
        assertEquals(CsvField.COMMAND.getDefaultValue(), result);
    }
    
    @Test
    void testProcessStandardField_withNoMappingForField() {
        ProductRow row = new ProductRow();
        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        // Don't add any mapping for TITLE
        
        // Act
        String result = FieldProcessor.processStandardField(CsvField.TITLE, row, mapping);
        
        // Assert
        String expectedDefault = CsvField.TITLE.getDefaultValue();
        assertEquals(expectedDefault != null ? expectedDefault : "", result);
    }
}