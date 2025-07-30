package com.medicisupply.util;

/**
 * Utility class for file name operations.
 */
public class FileNameUtil {
    
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
}