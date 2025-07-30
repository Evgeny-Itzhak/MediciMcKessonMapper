package com.medicisupply;

import com.medicisupply.config.AppConfig;
import com.medicisupply.config.AppConfigLoader;
import com.medicisupply.converter.XlsxToCsvConverter;
import com.medicisupply.context.AppContext;
import com.medicisupply.util.FileNameUtil;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        // Load application configuration (inputPath, outputPath, maxRowsPerFile)
        AppConfig config = AppConfigLoader.load(args);

        // Initialize AppContext with the source file name (e.g. "McKesson … 14092022.xlsx")
        String inputFileName = Paths.get(config.getInputPath()).getFileName().toString();
        AppContext.setSourceFileName(inputFileName);

        // Generate output file name based on input file name
        String outputFileName = FileNameUtil.generateOutputFileName(inputFileName);
        String outputDir = Paths.get(config.getOutputPath()).getParent().toString();
        String newOutputPath = Paths.get(outputDir, outputFileName).toString();
        
        // Update the config with the new output path
        config = new AppConfig(config.getInputPath(), newOutputPath, config.getMaxRowsPerFile());

        // Run the converter
        new XlsxToCsvConverter(config).convert();
    }
}