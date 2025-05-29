package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    @Test
    void testCsvOutputInMemory() throws Exception {
        // Prepare mock data
        ProductRow row = new ProductRow();
        row.setField("E1 SKU", "SKU123");
        row.setField("Retail Description", "Test Product");
        row.setField("Brand or Series", "Test Brand");

        List<ProductRow> data = Collections.singletonList(row);
        Map<String, CsvField> fieldMap = FieldMapper.getFieldMappings();

        // Use in-memory StringWriter instead of a file
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
        assertTrue(output.contains("MERGE"));  // COMMAND default
    }
}