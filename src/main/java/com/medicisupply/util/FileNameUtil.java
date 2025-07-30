package com.medicisupply.util;

import com.medicisupply.config.AppConfig;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

/**
 * Utility class for file name operations.
 */
@Log4j2
public class FileNameUtil {
    
    // Pattern to match dates in format "Month Day, Year" (e.g., "May 26, 2025")
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "(?i)(January|February|March|April|May|June|July|August|September|October|November|December)\\s+(\\d{1,2})\\s*,\\s*(\\d{4})");
    
    // Pattern to match dates in format "DDMMYYYY" (e.g., "14092022")
    private static final Pattern NUMERIC_DATE_PATTERN = Pattern.compile("(\\d{2})(\\d{2})(\\d{4})");
    
    /**
     * Generates output file name based on input file name according to the pattern:
     * output-{name_of_xlsx_file}.csv
     * where spaces in the xlsx filename are replaced with underscores and the .xlsx extension is removed
     * 
     * @param inputFileName the name of the input file
     * @return the generated output file name
     */
    public static String generateOutputFileName(String inputFileName) {
        // Remove extension
        String nameWithoutExtension = inputFileName.contains(".")
                ? inputFileName.substring(0, inputFileName.lastIndexOf('.'))
                : inputFileName;
        
        // Replace spaces with underscores
        String nameWithUnderscores = nameWithoutExtension.replace(' ', '_');
        
        // Create the new filename with "output-" prefix
        return "output-" + nameWithUnderscores + ".csv";
    }
    
    /**
     * Extracts year and month from the input file name.
     * Supports formats like "Month Day, Year" (e.g., "May 26, 2025") and "DDMMYYYY" (e.g., "14092022").
     * If no date is found, returns null.
     * 
     * @param inputFileName the name of the input file
     * @return YearMonth object containing the extracted year and month, or null if no date is found
     */
    public static YearMonth extractYearMonthFromFileName(String inputFileName) {
        LocalDate date = extractDateFromFileName(inputFileName);
        if (date == null) {
            return null;
        }
        return YearMonth.of(date.getYear(), date.getMonth());
    }
    
    /**
     * Extracts full date (year, month, day) from the input file name.
     * Supports formats like "Month Day, Year" (e.g., "May 26, 2025") and "DDMMYYYY" (e.g., "14092022").
     * If no date is found, returns null.
     * 
     * @param inputFileName the name of the input file
     * @return LocalDate object containing the extracted year, month, and day, or null if no date is found
     */
    public static LocalDate extractDateFromFileName(String inputFileName) {
        // Try to match "Month Day, Year" format
        Matcher matcher = DATE_PATTERN.matcher(inputFileName);
        if (matcher.find()) {
            String monthStr = matcher.group(1);
            int day = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));
            Month month = Month.valueOf(monthStr.toUpperCase().substring(0, 3) + monthStr.substring(3).toLowerCase());
            return LocalDate.of(year, month, day);
        }
        
        // Try to match "DDMMYYYY" format
        matcher = NUMERIC_DATE_PATTERN.matcher(inputFileName);
        if (matcher.find()) {
            int day = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));
            return LocalDate.of(year, month, day);
        }
        
        // If no date found, return null
        return null;
    }
    
    /**
     * Generates a directory path based on the year and month extracted from the input file name.
     * The directory follows the pattern {YYYY_MM}.
     * If no date is found in the input file name, returns the base directory path.
     * 
     * @param inputFileName the name of the input file
     * @param baseDir the base directory where the year-month directory will be created
     * @return the full path to the year-month directory or the base directory if no date is found
     * @deprecated Use {@link #generateYearMonthDayDirectory(String, String)} instead
     */
    @Deprecated
    public static String generateYearMonthDirectory(String inputFileName, String baseDir) {
        YearMonth yearMonth = extractYearMonthFromFileName(inputFileName);
        
        // If no date found in the file name, use the base directory
        if (yearMonth == null) {
            Path dirPath = Paths.get(baseDir);
            
            // Create the directory if it doesn't exist
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + dirPath, e);
            }
            
            return dirPath.toString();
        }
        
        // If date found, create a year-month subdirectory
        String dirName = String.format("%d_%02d", yearMonth.getYear(), yearMonth.getMonthValue());
        
        Path dirPath = Paths.get(baseDir, dirName);
        
        // Create the directory if it doesn't exist
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + dirPath, e);
        }
        
        return dirPath.toString();
    }
    
    /**
     * Generates a directory path based on the year, month, and day extracted from the input file name.
     * The directory follows the pattern {YYYY_MM/DD}.
     * If no date is found in the input file name, returns the base directory path.
     * 
     * @param inputFileName the name of the input file
     * @param baseDir the base directory where the year-month-day directory will be created
     * @return the full path to the year-month-day directory or the base directory if no date is found
     */
    public static String generateYearMonthDayDirectory(String inputFileName, String baseDir) {
        LocalDate date = extractDateFromFileName(inputFileName);
        
        // If no date found in the file name, use the base directory
        if (date == null) {
            Path dirPath = Paths.get(baseDir);
            
            // Create the directory if it doesn't exist
            try {
                Files.createDirectories(dirPath);
                log.debug("Created base directory: {}", dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + dirPath, e);
            }
            
            return dirPath.toString();
        }
        
        // If date found, create a year-month subdirectory with day subdirectory
        String yearMonthDirName = String.format("%d_%02d", date.getYear(), date.getMonthValue());
        String dayDirName = String.format("%02d", date.getDayOfMonth());
        
        Path yearMonthDirPath = Paths.get(baseDir, yearMonthDirName);
        Path dayDirPath = Paths.get(yearMonthDirPath.toString(), dayDirName);
        
        // Create the directories if they don't exist
        try {
            Files.createDirectories(dayDirPath);
            log.debug("Created directory: {}", dayDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + dayDirPath, e);
        }
        
        return dayDirPath.toString();
    }
    
    /**
     * Generates a complete output path for the CSV file based on the input path and base output directory.
     * This method:
     * 1. Extracts the input file name from the input path
     * 2. Generates the output file name based on the input file name
     * 3. Generates the year-month-day directory based on the input file name
     * 4. Combines the directory and file name to create the complete output path
     *
     * @param inputPath the full path to the input file
     * @param baseOutputDir the base directory for CSV output files
     * @return the complete output path for the CSV file
     * @deprecated Use {@link #generateOutputPath(String, AppConfig, String)} instead
     */
    @Deprecated
    public static String generateOutputPath(String inputPath, String baseOutputDir) {
        // Extract the input file name from the path
        String inputFileName = Paths.get(inputPath).getFileName().toString();
        
        // Generate output file name based on input file name
        String outputFileName = generateOutputFileName(inputFileName);
        
        // Create year-month-day directory based on input file name under the base output directory
        String yearMonthDayDir = generateYearMonthDayDirectory(inputFileName, baseOutputDir);
        
        // Combine the directory and file name to create the complete output path
        return Paths.get(yearMonthDayDir, outputFileName).toString();
    }
    
    /**
     * Generates a complete output path for the CSV file based on the input path, AppConfig, and default output directory.
     * This method handles four scenarios:
     * 1. No date in filename + no outputFilePath specified → generate under defaultOutputDir
     * 2. No date in filename + outputFilePath specified → generate under the specified outputFilePath
     * 3. Date in filename + outputFilePath specified → generate under outputFilePath/YYYY_MM/DD/
     * 4. Date in filename + no outputFilePath specified → generate under defaultOutputDir/YYYY_MM/DD/
     *
     * @param inputPath the full path to the input file
     * @param config the AppConfig object containing configuration information
     * @param defaultOutputDir the default output directory to use if no outputFilePath is specified
     * @return the complete output path for the CSV file
     */
    public static String generateOutputPath(String inputPath, AppConfig config, String defaultOutputDir) {
        // Extract the input file name from the path
        String inputFileName = Paths.get(inputPath).getFileName().toString();
        
        // Generate output file name based on input file name
        String outputFileName = generateOutputFileName(inputFileName);
        
        // Check if the input file name contains a date
        LocalDate date = extractDateFromFileName(inputFileName);
        boolean hasDate = (date != null);
        
        // Check if outputFilePath was specified
        boolean outputPathSpecified = config.isOutputPathSpecified();
        
        // Get the base directory to use
        String baseDir;
        if (outputPathSpecified) {
            // If outputFilePath was specified, use the parent directory of the specified outputFilePath
            File outputFile = new File(config.getOutputPath());
            baseDir = outputFile.getParent();
            if (baseDir == null) {
                // If the outputFilePath doesn't have a parent directory, use the current directory
                baseDir = ".";
            }
            log.debug("Using specified output directory: {}", baseDir);
        } else {
            // If outputFilePath was not specified, use the default output directory
            baseDir = defaultOutputDir;
            log.debug("Using default output directory: {}", baseDir);
        }
        
        // Generate the output path based on the scenario
        String outputPath;
        if (hasDate) {
            // Scenarios 3 and 4: Date in filename
            String yearMonthDir = String.format("%d_%02d", date.getYear(), date.getMonthValue());
            String dayDir = String.format("%02d", date.getDayOfMonth());
            Path dirPath = Paths.get(baseDir, yearMonthDir, dayDir);
            
            // Create the directory if it doesn't exist
            try {
                Files.createDirectories(dirPath);
                log.debug("Created directory: {}", dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + dirPath, e);
            }
            
            outputPath = Paths.get(dirPath.toString(), outputFileName).toString();
            log.info("Generated output path for file with date: {}", outputPath);
        } else {
            // Scenarios 1 and 2: No date in filename
            Path dirPath = Paths.get(baseDir);
            
            // Create the directory if it doesn't exist
            try {
                Files.createDirectories(dirPath);
                log.debug("Created directory: {}", dirPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + dirPath, e);
            }
            
            outputPath = Paths.get(dirPath.toString(), outputFileName).toString();
            log.info("Generated output path for file without date: {}", outputPath);
        }
        
        return outputPath;
    }
}