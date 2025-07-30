package com.medicisupply.service;

import com.medicisupply.config.AppConfig;
import com.medicisupply.config.AppConfigLoader;
import com.medicisupply.context.AppContext;
import com.medicisupply.converter.XlsxToCsvConverter;
import com.medicisupply.util.FileNameUtil;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Paths;

/**
 * Service class responsible for handling the Excel to CSV conversion workflow.
 */
@Log4j2
public class ConversionService {

    /**
     * Converts an Excel file to CSV format.
     * This method:
     * 1. Loads the application configuration
     * 2. Sets up the AppContext with the source file name
     * 3. Generates the output path based on the input path and base output directory
     * 4. Updates the configuration with the new output path
     * 5. Runs the converter
     *
     * @param args command line arguments
     */
    public void convert(String[] args) {
        // Load application configuration (inputPath, outputPath, maxRowsPerFile)
        AppConfig config = AppConfigLoader.load(args);

        // Initialize AppContext with the source file name (e.g. "McKesson … 14092022.xlsx")
        String inputFileName = Paths.get(config.getInputPath()).getFileName().toString();
        AppContext.setSourceFileName(inputFileName);

        // Generate output path based on input path and base output directory
        String outputPath = FileNameUtil.generateOutputPath(
                config.getInputPath(), 
                AppConfigLoader.OUTPUT_DIR
        );

        // Update the config with the new output path
        config = new AppConfig(config.getInputPath(), outputPath, config.getMaxRowsPerFile());

        // Run the converter
        log.info("Starting conversion from {} to {}", config.getInputPath(), config.getOutputPath());
        new XlsxToCsvConverter(config).convert();
        log.info("Conversion completed successfully");
    }
}