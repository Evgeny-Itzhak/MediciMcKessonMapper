package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import com.medicisupply.writer.CsvWriter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class CsvWriterTest {

    @Test
    void testCsvOutputInMemory() {
        ProductRow row = new ProductRow();
        row.setField("E1 SKU", "SKU123");
        row.setField("Retail Description", "Test Product");
        row.setField("Brand or Series", "Test Brand");

        List<ProductRow> data = Collections.singletonList(row);
        Map<String, CsvField> fieldMap = FieldMapper.getFieldMappings();

        StringWriter stringWriter = new StringWriter();
        try (PrintWriter writer = new PrintWriter(stringWriter)) {
            List<String> headers = new ArrayList<>();
            for (CsvField field : fieldMap.values()) {
                headers.add(field.getHeader());
            }
            writer.println(String.join(",", headers));

            for (ProductRow r : data) {
                List<String> values = new ArrayList<>();
                for (CsvField field : fieldMap.values()) {
                    values.add(FieldProcessor.process(field, r));
                }
                writer.println(String.join(",", values));
            }
        }

        String output = stringWriter.toString();
        assertTrue(output.contains("Handle"));
        assertTrue(output.contains("SKU123"));
        assertTrue(output.contains("MERGE"));
    }

    @Test
    void testSingleFileWhenMaxRowsIsNegative() throws Exception {
        CsvWriter writer = new CsvWriter();
        List<ProductRow> rows = generateRows(5);
        Map<String, CsvField> fieldMap = getFullFieldMap();

        File tempDir = Files.createTempDirectory("nosplit-test").toFile();
        File output = new File(tempDir, "output.csv");
        writer.write(output, rows, fieldMap, -1);

        File[] files = tempDir.listFiles((dir, name) -> name.endsWith(".csv"));
        assertNotNull(files);
        assertEquals(1, files.length);
        assertEquals("output.csv", files[0].getName());

        cleanupTempDir(tempDir);
    }

    @Test
    void testMultipleFilesWrittenWhenMaxRowsIsTwo() throws Exception {
        CsvWriter writer = new CsvWriter();
        List<ProductRow> rows = generateRows(5);
        Map<String, CsvField> fieldMap = getFullFieldMap();

        File tempDir = Files.createTempDirectory("split2-test").toFile();
        File output = new File(tempDir, "split.csv");
        writer.write(output, rows, fieldMap, 2);

        File[] files = tempDir.listFiles((dir, name) -> name.matches("split_part\\d+\\.csv"));
        assertNotNull(files);
        assertEquals(3, files.length); // 2 + 2 + 1 rows

        cleanupTempDir(tempDir);
    }

    @Test
    void testSingleFileWhenMaxRowsExceedsDataSize() throws Exception {
        CsvWriter writer = new CsvWriter();
        List<ProductRow> rows = generateRows(3);
        Map<String, CsvField> fieldMap = getFullFieldMap();

        File tempDir = Files.createTempDirectory("exceed-test").toFile();
        File output = new File(tempDir, "single.csv");
        writer.write(output, rows, fieldMap, 10); // maxRows > total rows

        File[] files = tempDir.listFiles((dir, name) -> name.endsWith(".csv"));
        assertNotNull(files);
        assertEquals(1, files.length);
        assertEquals("single_part1.csv", files[0].getName());

        cleanupTempDir(tempDir);
    }

    @Test
    void testEachRowGetsOwnFileWhenMaxRowsIsOne() throws Exception {
        CsvWriter writer = new CsvWriter();
        List<ProductRow> rows = generateRows(3);
        Map<String, CsvField> fieldMap = getFullFieldMap();

        File tempDir = Files.createTempDirectory("one-each-test").toFile();
        File output = new File(tempDir, "split.csv");
        writer.write(output, rows, fieldMap, 1);

        File[] files = tempDir.listFiles((dir, name) -> name.matches("split_part\\d+\\.csv"));
        assertNotNull(files);
        assertEquals(3, files.length);

        cleanupTempDir(tempDir);
    }

    // 🔧 Utility methods

    private List<ProductRow> generateRows(int count) {
        List<ProductRow> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Map<String, String> row = new HashMap<>();
            row.put("E1 SKU", "SKU" + i);
            row.put("Retail Description", "Product " + i);
            row.put("Brand or Series", "BrandX");
            list.add(new ProductRow(row));
        }
        return list;
    }

    private Map<String, CsvField> getFullFieldMap() {
        Map<String, CsvField> fieldMap = new LinkedHashMap<>();
        for (CsvField field : CsvField.values()) {
            fieldMap.put(field.getHeader(), field);
        }
        return fieldMap;
    }

    private void cleanupTempDir(File dir) {
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            boolean deleted = f.delete();
            if (!deleted) {
                log.warn("Failed to delete temp file: {}", f.getAbsolutePath());
            }
        }

        boolean dirDeleted = dir.delete();
        if (!dirDeleted) {
            log.warn("Failed to delete temp directory: {}", dir.getAbsolutePath());
        }
    }


}
