package com.medicisupply.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppConfig {
    private final String inputPath;
    private final String outputPath;
    private final int maxRowsPerFile;
}