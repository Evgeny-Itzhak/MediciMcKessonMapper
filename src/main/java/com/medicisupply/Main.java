package com.medicisupply;

import com.medicisupply.config.AppConfig;
import com.medicisupply.config.AppConfigLoader;
import com.medicisupply.converter.XlsxToCsvConverter;
import com.medicisupply.context.AppContext;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        // Load application configuration (inputPath, outputPath, maxRowsPerFile)
        AppConfig config = AppConfigLoader.load(args);

        // Initialize AppContext with the source file name (e.g. "McKesson … 14092022.xlsx")
        String inputFileName = Paths.get(config.getInputPath()).getFileName().toString();
        AppContext.setSourceFileName(inputFileName);

        // Run the converter
        new XlsxToCsvConverter(config).convert();
    }
}