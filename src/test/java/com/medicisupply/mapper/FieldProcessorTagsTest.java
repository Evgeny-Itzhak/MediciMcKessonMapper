package com.medicisupply.mapper;

import com.medicisupply.context.AppContext;
import com.medicisupply.model.ProductRow;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldProcessorTagsTest {

    @Test
    void testBuildTags_numericSuffix() {
        // Numeric DDMMYYYY suffix
        AppContext.setSourceFileName("McKesson eCommerce Formulary Weekly 14092022.xlsx");

        ProductRow row = new ProductRow();
        row.setField("Supply Manager Category", "Stockings and Socks");

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        mapping.put(CsvField.TAGS, "Supply Manager Category");

        String tags = FieldProcessor.process(CsvField.TAGS, row, mapping);

        assertEquals(
                "__Mckesson-data,__Mckesson-upload-14092022,__Mckesson-September-Sheet-1,Stockings and Socks",
                tags
        );
    }

    @Test
    void testBuildTags_englishDate() {
        // English format "MMMM d, yyyy"
        AppContext.setSourceFileName("McKesson eCommerce Formulary Weekly May 26, 2025.xlsx");

        ProductRow row = new ProductRow();
        row.setField("Supply Manager Category", "Handbags");

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        mapping.put(CsvField.TAGS, "Supply Manager Category");

        String tags = FieldProcessor.process(CsvField.TAGS, row, mapping);

        assertEquals(
                "__Mckesson-data,__Mckesson-upload-26052025,__Mckesson-May-Sheet-1,Handbags",
                tags
        );
    }

    @Test
    void testBuildTags_missingDate() {
        // No date info at all
        AppContext.setSourceFileName("badname.xlsx");

        ProductRow row = new ProductRow();
        row.setField("Supply Manager Category", "Some Category");

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        mapping.put(CsvField.TAGS, "Supply Manager Category");

        String tags = FieldProcessor.process(CsvField.TAGS, row, mapping);

        assertEquals(
                "Some Category",
                tags
        );
    }

    @Test
    void testBuildTags_unparseableDate() {
        // Suffix matches \\d{8} but date is invalid → fallback to category
        AppContext.setSourceFileName("McKesson eCommerce Formulary Weekly 99999999.xlsx");

        ProductRow row = new ProductRow();
        row.setField("Supply Manager Category", "Other Category");

        Map<CsvField, String> mapping = new EnumMap<>(CsvField.class);
        mapping.put(CsvField.TAGS, "Supply Manager Category");

        String tags = FieldProcessor.process(CsvField.TAGS, row, mapping);

        assertEquals(
                "Other Category",
                tags
        );
    }
}