package com.medicisupply.converter;

import com.medicisupply.config.AppConfig;
import com.medicisupply.exception.CsvConversionException;
import com.medicisupply.mapper.FieldMapper;
import com.medicisupply.model.ProductRow;
import com.medicisupply.reader.ExcelReader;
import com.medicisupply.writer.CsvWriter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.List;

@Log4j2
public class XlsxToCsvConverter {
    private final AppConfig config;

    public XlsxToCsvConverter(AppConfig config) {
        this.config = config;
    }

    public void convert() {
        try {
            log.info("Reading Excel file: " + config.getInputPath());
            ExcelReader reader = new ExcelReader();
            List<ProductRow> rows = reader.read(new File(config.getInputPath()));

            CsvWriter writer = new CsvWriter();
            writer.write(
                    new File(config.getOutputPath()),
                    rows,
                    FieldMapper.getFieldMappings(),
                    config.getMaxRowsPerFile()
            );

            log.info("CSV successfully written to " + config.getOutputPath());
        } catch (Exception e) {
            throw new CsvConversionException("Failed to convert XLSX to CSV", e);
        }
    }
}
