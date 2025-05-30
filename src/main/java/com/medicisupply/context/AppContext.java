package com.medicisupply.context;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Holds global parameters of the current run,
 * in particular the source Excel file name and pre-parsed date info.
 */
@Log4j2
public class AppContext {

    @Getter
    private static String sourceFileName;

    @Getter
    private static String datePart;    // e.g. "14092022"

    @Getter
    private static String monthName;   // e.g. "September"

    /**
     * Set the name of the source file and parse datePart & monthName once.
     * Supports both "… 14092022.xlsx" and "… May 26, 2025.xlsx".
     */
    public static void setSourceFileName(String fileName) {
        sourceFileName = fileName;
        log.info("Source file set to: {}", fileName);

        // 1) strip extension
        String base = fileName.contains(".")
                ? fileName.substring(0, fileName.lastIndexOf('.'))
                : fileName;

        // 2) try simple DDMMYYYY suffix
        String[] tokens = base.split(" ");
        String last = tokens[tokens.length - 1];
        if (last.matches("\\d{8}")) {
            // format is already DDMMYYYY
            datePart = last;
            parseMonthFromDatePart();
            return;
        }

        // 3) fallback: try parse "MMMM d, yyyy"
        //    e.g. "May 26, 2025"
        String possibleDate = "";
        // join last three tokens: monthName + dayComma + year
        if (tokens.length >= 3) {
            int n = tokens.length;
            possibleDate = tokens[n-3] + " " + tokens[n-2] + " " + tokens[n-1];
        }
        try {
            DateTimeFormatter inFmt = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            LocalDate dt = LocalDate.parse(possibleDate, inFmt);
            datePart = dt.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            monthName = dt.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        } catch (Exception ex) {
            log.error("Failed to extract date from file name \"{}\": {}", fileName, ex.getMessage());
            datePart = null;
            monthName = null;
        }
    }

    /**
     * Parse monthName from already-set datePart.
     * Called when datePart is in DDMMYYYY format.
     */
    private static void parseMonthFromDatePart() {
        try {
            LocalDate dt = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("ddMMyyyy"));
            monthName = dt.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        } catch (Exception ex) {
            log.error("Failed to parse month from datePart \"{}\": {}", datePart, ex.getMessage());
            monthName = null;
        }
    }
}