package com.medicisupply.writer;

import com.medicisupply.mapper.CsvField;
import com.medicisupply.mapper.FieldProcessor;
import com.medicisupply.model.ProductRow;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class CsvWriter {

    public void write(File output,
                      List<ProductRow> data,
                      Map<CsvField, String> mapping,
                      int maxRows) throws IOException {

        // Build the header row from the mapping keys
        List<String> headers = new ArrayList<>();
        for (CsvField field : mapping.keySet()) {
            headers.add(field.getHeader());
        }

        if (maxRows <= 0) {
            log.info("Writing single CSV file: {}", output.getAbsolutePath());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                writer.write(String.join(",", headers));
                writer.newLine();

                int rowCount = 0;
                for (ProductRow row : data) {
                    List<String> values = new ArrayList<>();
                    for (CsvField field : mapping.keySet()) {
                        // Process the field value
                        String processedValue = FieldProcessor.process(field, row, mapping);

                        // If the processed value is null or empty and a default exists, use it
                        if ((processedValue == null || processedValue.isEmpty())
                                && field.getDefaultValue() != null) {
                            processedValue = field.getDefaultValue();
                        }

                        values.add(escape(processedValue));
                        log.debug("Row {} - Field '{}' = '{}'",
                                rowCount + 1, field.getHeader(), processedValue);
                    }
                    writer.write(String.join(",", values));
                    writer.newLine();
                    rowCount++;
                }

                log.info("✅ Wrote single CSV with {} rows", rowCount);
            }
            return;
        }

        // Split into multiple CSV parts if maxRows > 0
        log.info("Writing CSV in chunks of {} rows max to base: {}",
                maxRows, output.getAbsolutePath());
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
                    for (CsvField field : mapping.keySet()) {
                        // Process the field value
                        String processedValue = FieldProcessor.process(field, row, mapping);

                        // Apply default if necessary
                        if ((processedValue == null || processedValue.isEmpty())
                                && field.getDefaultValue() != null) {
                            processedValue = field.getDefaultValue();
                        }

                        values.add(escape(processedValue));
                        log.debug("Part {} - Row {} - Field '{}' = '{}'",
                                part, rowCount + 1, field.getHeader(), processedValue);
                    }
                    writer.write(String.join(",", values));
                    writer.newLine();
                    rowCount++;
                }

                log.info("✅ Part {} written: {} ({} rows)",
                        part, partFile.getAbsolutePath(), rowCount);
                part++;
            }
        }
    }

    /**
     * Escape a CSV field: wrap in quotes if it contains comma or quote,
     * doubling any internal quotes.
     */
    private String escape(String s) {
        if (s == null) {
            return "";
        }
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
