package com.medicisupply.util;

import java.time.Month;
import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Utility class for file name operations.
 */
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
        // Try to match "Month Day, Year" format
        Matcher matcher = DATE_PATTERN.matcher(inputFileName);
        if (matcher.find()) {
            String monthStr = matcher.group(1);
            int year = Integer.parseInt(matcher.group(3));
            Month month = Month.valueOf(monthStr.toUpperCase().substring(0, 3) + monthStr.substring(3).toLowerCase());
            return YearMonth.of(year, month);
        }
        
        // Try to match "DDMMYYYY" format
        matcher = NUMERIC_DATE_PATTERN.matcher(inputFileName);
        if (matcher.find()) {
            int month = Integer.parseInt(matcher.group(2));
            int year = Integer.parseInt(matcher.group(3));
            return YearMonth.of(year, month);
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
     */
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
     * Generates a complete output path for the CSV file based on the input path and base output directory.
     * This method:
     * 1. Extracts the input file name from the input path
     * 2. Generates the output file name based on the input file name
     * 3. Generates the year-month directory based on the input file name
     * 4. Combines the directory and file name to create the complete output path
     *
     * @param inputPath the full path to the input file
     * @param baseOutputDir the base directory for CSV output files
     * @return the complete output path for the CSV file
     */
    public static String generateOutputPath(String inputPath, String baseOutputDir) {
        // Extract the input file name from the path
        String inputFileName = Paths.get(inputPath).getFileName().toString();
        
        // Generate output file name based on input file name
        String outputFileName = generateOutputFileName(inputFileName);
        
        // Create year-month directory based on input file name under the base output directory
        String yearMonthDir = generateYearMonthDirectory(inputFileName, baseOutputDir);
        
        // Combine the directory and file name to create the complete output path
        return Paths.get(yearMonthDir, outputFileName).toString();
    }
}