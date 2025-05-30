package com.medicisupply.context;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AppContext.setSourceFileName(...) parsing logic.
 */
class AppContextTest {

    @Test
    void testNumericDateSuffixParsing() {
        // Should parse DDMMYYYY suffix and derive month name
        AppContext.setSourceFileName("McKesson eCommerce Formulary Weekly 14092022.xlsx");
        assertEquals("14092022", AppContext.getDatePart(), "Date part must be extracted as DDMMYYYY");
        assertEquals("September", AppContext.getMonthName(), "Month name must be full English");
    }

    @Test
    void testEnglishDateParsing() {
        // Should parse "MMMM d, yyyy" format and convert to DDMMYYYY + month
        AppContext.setSourceFileName("McKesson eCommerce Formulary Weekly May 26, 2025.xlsx");
        assertEquals("26052025", AppContext.getDatePart(), "Date part must be converted to DDMMYYYY");
        assertEquals("May", AppContext.getMonthName(), "Month name must be full English for May");
    }

    @Test
    void testInvalidFileNameResultsInNull() {
        // When no date info present, both getters must return null
        AppContext.setSourceFileName("McKesson eCommerce Formulary Weekly.xlsx");
        assertNull(AppContext.getDatePart(), "Date part must be null on invalid filename");
        assertNull(AppContext.getMonthName(), "Month name must be null on invalid filename");
    }
}