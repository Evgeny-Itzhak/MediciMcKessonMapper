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
        
        // Debug log to see the output path loaded by AppConfigLoader
        log.debug("Output path from AppConfigLoader: {}", config.getOutputPath());

        // Initialize AppContext with the source file name (e.g. "McKesson … 14092022.xlsx")
        String inputFileName = Paths.get(config.getInputPath()).getFileName().toString();
        AppContext.setSourceFileName(inputFileName);

        // Log the output path before generating the final path
        log.info("Initial output path: {}", config.getOutputPath());
        
        // Generate the final output path based on the four scenarios
        String outputPath = FileNameUtil.generateOutputPath(
            config.getInputPath(),
            config,
            AppConfigLoader.OUTPUT_DIR
        );
        
        // Create a new AppConfig object with the updated output path
        AppConfig updatedConfig = new AppConfig(
            config.getInputPath(),
            outputPath,
            config.getMaxRowsPerFile(),
            config.isOutputPathSpecified()
        );
        
        // Log the final output path
        log.info("Final output path: {}", updatedConfig.getOutputPath());
        
        // Run the converter with the updated config
        log.info("Starting conversion from {} to {}", updatedConfig.getInputPath(), updatedConfig.getOutputPath());
        new XlsxToCsvConverter(updatedConfig).convert();
        
        log.info("Conversion completed successfully");
    }
}