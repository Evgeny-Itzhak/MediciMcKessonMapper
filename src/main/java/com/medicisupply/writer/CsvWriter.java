package com.medicisupply.writer;

import com.medicisupply.mapper.*;
import com.medicisupply.model.ProductRow;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.*;

@Log4j2
public class CsvWriter {

    public void write(File output, List<ProductRow> data, Map<String, CsvField> map) throws IOException {
        log.info("Starting to write CSV to: {}", output.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {

            List<String> headers = new ArrayList<>();
            for (CsvField f : map.values()) {
                headers.add(f.getHeader());
            }

            writer.write(String.join(",", headers));
            writer.newLine();
            log.debug("CSV headers written: {}", headers);

            int rowCount = 0;
            for (ProductRow row : data) {
                List<String> values = new ArrayList<>();
                for (CsvField field : map.values()) {
                    String processedValue = FieldProcessor.process(field, row);
                    values.add(escape(processedValue));
                    log.debug("Row {} - Field '{}' = '{}'", rowCount + 1, field.getHeader(), processedValue);
                }
                writer.write(String.join(",", values));
                writer.newLine();
                rowCount++;
            }

            log.info("Successfully wrote {} rows to {}", rowCount, output.getName());

        } catch (IOException e) {
            log.error("Failed to write CSV file: {}", output.getAbsolutePath(), e);
            throw e;
        }
    }

    private String escape(String s) {
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
