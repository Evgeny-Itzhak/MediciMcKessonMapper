package com.medicisupply.writer;

import com.medicisupply.mapper.*;
import com.medicisupply.model.ProductRow;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.*;

@Log4j2
public class CsvWriter {

    public void write(File output, List<ProductRow> data, Map<String, CsvField> map, int maxRows) throws IOException {
        List<String> headers = new ArrayList<>();
        for (CsvField f : map.values()) {
            headers.add(f.getHeader());
        }

        if (maxRows <= 0) {
            // Write single file
            log.info("Writing single CSV file: {}", output.getAbsolutePath());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                writer.write(String.join(",", headers));
                writer.newLine();

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

                log.info("✅ Wrote single CSV with {} rows", rowCount);
            }
            return;
        }

        // Write in chunks
        log.info("Writing CSV in chunks of {} rows max to base: {}", maxRows, output.getAbsolutePath());

        String baseName = output.getName().replaceFirst("\\.csv$", "");
        String parent = output.getParent();

        int part = 1;
        for (int i = 0; i < data.size(); i += maxRows) {
            String partFileName = baseName + "_part" + part + ".csv";
            File partFile = new File(parent, partFileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(partFile))) {
                writer.write(String.join(",", headers));
                writer.newLine();

                List<ProductRow> chunk = data.subList(i, Math.min(i + maxRows, data.size()));
                int rowCount = 0;
                for (ProductRow row : chunk) {
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

                log.info("✅ Part {} written: {} ({} rows)", part, partFile.getAbsolutePath(), rowCount);
                part++;
            }
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
