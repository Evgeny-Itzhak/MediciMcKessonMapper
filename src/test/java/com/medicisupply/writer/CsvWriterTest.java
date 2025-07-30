package com.medicisupply.writer;

import com.medicisupply.mapper.CsvField;
import com.medicisupply.mapper.FieldMapper;
import com.medicisupply.model.ProductRow;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    @Test
    void testCsvOutputInMemory() throws Exception {
        // --- Prepare mock data ---
        ProductRow row = new ProductRow();
        row.setField("Retail Description", "Test Product");
        row.setField("McK Item No", "12345");
        row.setField("Manufacturer Number", "MN6789");
        row.setField("Brand or Series", "Test Brand");
        row.setField("Retail Features & Benefits", "<p>Description</p>");
        row.setField("Application", "TestText");

        List<ProductRow> data = Collections.singletonList(row);
        Map<CsvField, String> fieldMap = FieldMapper.getFieldMappings();

        // --- Use CsvWriter to dump into a temp CSV file ---
        File tempCsv = Files.createTempFile("csv-output-", ".csv").toFile();
        tempCsv.deleteOnExit();

        // maxRows=0 → single file
        new CsvWriter().write(tempCsv, data, fieldMap, 0);

        // Read entire file into a string
        String output = Files.readString(tempCsv.toPath(), StandardCharsets.UTF_8);

        // --- Assertions ---

        // Header and default COMMAND check
        assertTrue(output.contains("Handle"),
                "ERROR: 'Handle' was not found in the output");
        assertTrue(output.contains("MERGE"),
                "ERROR: 'MERGE' was not found in the output (default for COMMAND)");

        // New HANDLE format: "Retail Description MSC-MCK-<McK Item No> <Manufacturer Number>"
        assertTrue(output.contains("Test Product MSC-MCK-12345 MN6789"),
                "ERROR: built Handle value was not found in the output");

        // Other fields
        assertTrue(output.contains("Test Product"),
                "ERROR: 'Test Product' was not found in the output (mapped from 'Retail Description')");
        assertTrue(output.contains("Test Brand"),
                "ERROR: 'Test Brand' was not found in the output");
        assertTrue(output.contains("<p>Description</p>"),
                "ERROR: '<p>Description</p>' was not found in the output");
        assertTrue(output.contains("REPLACE"),
                "ERROR: 'REPLACE' was not found in the output (default for Tags Command)");
    }

    @Test
    void testCsvSplittingByMaxRows() throws Exception {
        // --- Prepare mock data ---
        List<ProductRow> data = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ProductRow row = new ProductRow();
            row.setField("Retail Description", "Product " + i);
            row.setField("Brand or Series",    "Brand "   + i);
            data.add(row);
        }
        Map<CsvField, String> fieldMap = FieldMapper.getFieldMappings();

        // --- Prepare temp directory and base file name ---
        File tempDir = Files.createTempDirectory("test-output").toFile();
        tempDir.deleteOnExit();
        File baseCsv = new File(tempDir, "test_output.csv");

        // Write with maxRows = 2 → should produce 3 files: _part1, _part2, _part3
        new CsvWriter().write(baseCsv, data, fieldMap, 2);

        File part1 = new File(tempDir, "test_output_part1.csv");
        File part2 = new File(tempDir, "test_output_part2.csv");
        File part3 = new File(tempDir, "test_output_part3.csv");

        assertTrue(part1.exists(), "Part1 file was not created");
        assertTrue(part2.exists(), "Part2 file was not created");
        assertTrue(part3.exists(), "Part3 file was not created");

        // Expect exactly 3 CSV files in the directory
        String[] csvFiles = tempDir.list((dir, name) -> name.endsWith(".csv"));
        assertNotNull(csvFiles);
        assertEquals(3, csvFiles.length, "Expected 3 CSV parts, but found " + csvFiles.length);
    }
    
    @Test
    void testCsvEscaping() throws Exception {
        // --- Prepare mock data with fields that need escaping ---
        ProductRow row = new ProductRow();
        
        // Field with commas
        row.setField("Retail Description", "Product, with commas");
        
        // Field with quotes
        row.setField("Brand or Series", "Brand \"quoted\" text");
        
        // Field with both commas and quotes
        row.setField("Retail Features & Benefits", "Features, with \"quotes\" and commas");
        
        // Regular field
        row.setField("Application", "Regular text");
        
        // Required fields for handle building
        row.setField("McK Item No", "12345");
        
        List<ProductRow> data = Collections.singletonList(row);
        Map<CsvField, String> fieldMap = FieldMapper.getFieldMappings();

        // --- Use CsvWriter to dump into a temp CSV file ---
        File tempCsv = Files.createTempFile("csv-escaping-", ".csv").toFile();
        tempCsv.deleteOnExit();

        new CsvWriter().write(tempCsv, data, fieldMap, 0);

        // Read entire file into a string
        String output = Files.readString(tempCsv.toPath(), StandardCharsets.UTF_8);
        
        // --- Assertions ---
        
        // Check that fields with commas are wrapped in quotes
        assertTrue(output.contains("\"Product, with commas\""), 
                "Field with commas should be wrapped in quotes");
        
        // Check that fields with quotes have their quotes doubled and are wrapped in quotes
        assertTrue(output.contains("\"Brand \"\"quoted\"\" text\""), 
                "Field with quotes should have quotes doubled and be wrapped in quotes");
        
        // Check that fields with both commas and quotes are properly escaped
        assertTrue(output.contains("\"Features, with \"\"quotes\"\" and commas\""), 
                "Field with both commas and quotes should be properly escaped");
        
        // Check that regular fields are not wrapped in quotes
        assertTrue(output.contains(",Regular text,"), 
                "Regular field should not be wrapped in quotes");
    }
}