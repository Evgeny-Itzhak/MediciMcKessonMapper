package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import com.medicisupply.writer.CsvWriter;
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
        row.setField("Brand or Series",   "Test Brand");
        row.setField("Body HTML",         "<p>Description</p>");
        row.setField("TestColumn",        "TestValue");

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
        assertTrue(
                output.contains("Handle"),
                "ERROR: 'Handle' was not found in the output"
        );

        assertTrue(
                output.contains("MERGE"),
                "ERROR: 'MERGE' was not found in the output (default for COMMAND)"
        );

        assertTrue(
                output.contains("Test Product"),
                "ERROR: 'Test Product' was not found in the output (mapped from 'Retail Description')"
        );

        assertTrue(
                output.contains("Test Brand"),
                "ERROR: 'Test Brand' was not found in the output"
        );

        assertTrue(
                output.contains("<p>Description</p>"),
                "ERROR: '<p>Description</p>' was not found in the output"
        );

        assertTrue(
                output.contains("TestValue"),
                "ERROR: 'TestValue' was not found in the output"
        );
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
}
