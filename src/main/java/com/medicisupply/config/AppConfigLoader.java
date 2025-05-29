package com.medicisupply.config;

import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class AppConfigLoader {
    private static final String INPUT_DIR = "McKesson-eCommerceFormularyWeekly";
    private static final String OUTPUT_DIR = "csv-McKessonMappingResults";
    private static final String DEFAULT_INPUT_FILE = "input.xlsx";
    private static final String DEFAULT_OUTPUT_FILE = "output.csv";
    private static final String YAML_FILE = "config.yaml";

    public static AppConfig load(String[] args) {

        // 1. CLI args
        Map<String, String> cliArgs = parseArgs(args);
        Map<String, String> config = new HashMap<>(cliArgs);
        if (!cliArgs.isEmpty()) {
            log.info("Loaded CLI arguments: {}", cliArgs);
        }

        // 2. ENV variables
        Map<String, String> env = System.getenv();
        if (env.containsKey("INPUT_FILE_PATH")) {
            config.putIfAbsent("inputFilePath", env.get("INPUT_FILE_PATH"));
            log.info("Loaded input path from ENV: {}", env.get("INPUT_FILE_PATH"));
        }
        if (env.containsKey("OUTPUT_FILE_PATH")) {
            config.putIfAbsent("outputFilePath", env.get("OUTPUT_FILE_PATH"));
            log.info("Loaded output path from ENV: {}", env.get("OUTPUT_FILE_PATH"));
        }

        // 3. YAML config
        Map<String, Object> yamlMap = null;
        if (!config.containsKey("inputFilePath") || !config.containsKey("outputFilePath")) {
            try (InputStream input = AppConfigLoader.class.getClassLoader().getResourceAsStream(YAML_FILE)) {
                if (input != null) {
                    Yaml yaml = new Yaml();
                    yamlMap = yaml.load(input);
                    log.info("Parsed YAML: {}", yamlMap);
                    if (yamlMap != null) {
                        config.putIfAbsent("inputFilePath", (String) yamlMap.get("inputFilePath"));
                        config.putIfAbsent("outputFilePath", (String) yamlMap.get("outputFilePath"));
                    }
                } else {
                    log.warn("config.yaml not found on classpath.");
                }
            } catch (Exception e) {
                log.error("Failed to load YAML config: {}", e.getMessage(), e);
            }
        }

        // 4. Fallback defaults
        String inputPath = config.getOrDefault("inputFilePath",
                new File(INPUT_DIR, DEFAULT_INPUT_FILE).getAbsolutePath());

        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        String outputPath = config.getOrDefault("outputFilePath",
                new File(outputDir, DEFAULT_OUTPUT_FILE).getAbsolutePath());

        // 5. Logs and return
        createLogsDirectory();
        log.info("Final INPUT path: {}", inputPath);
        log.info("Final OUTPUT path: {}", outputPath);
        log.info("Working directory: {}", System.getProperty("user.dir"));


        int maxRows = -1; // default: no splitting
        if (cliArgs.containsKey("maxRowsPerFile")) {
            maxRows = Integer.parseInt(cliArgs.get("maxRowsPerFile"));
            log.info("Loaded maxRowsPerFile from CLI: {}", maxRows);
        } else if (env.containsKey("MAX_ROWS_PER_FILE")) {
            maxRows = Integer.parseInt(env.get("MAX_ROWS_PER_FILE"));
            log.info("Loaded maxRowsPerFile from ENV: {}", maxRows);
        } else if (yamlMap != null && yamlMap.containsKey("maxRowsPerFile")) {
            maxRows = (int) yamlMap.get("maxRowsPerFile");
            log.info("Loaded maxRowsPerFile from YAML: {}", maxRows);
        }

        return new AppConfig(inputPath, outputPath, maxRows);

    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("-") && arg.contains("=")) {
                String[] split = arg.substring(1).split("=", 2);
                if (split.length == 2) {
                    map.put(split[0], split[1]);
                }
            }
        }
        return map;
    }

    private static void createLogsDirectory() {
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }
}
