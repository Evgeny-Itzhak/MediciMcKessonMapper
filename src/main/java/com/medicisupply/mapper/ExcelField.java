package com.medicisupply.mapper;

import lombok.Getter;

/**
 * Defines all the columns read from the input Excel file.
 * Returns the exact header name as it appears in the Excel file.
 */
@Getter
public enum ExcelField {
    RETAIL_DESCRIPTION("Retail Description"),
    COMMAND("Command"),
    RETAIL_FEATURES_AND_BENEFITS("Retail Features & Benefits"),
    BRAND_OR_SERIES("Brand or Series"),
    TESTCOLUMN("TestColumn"),
    MCK_ITEM_NO("McK Item No"),
    MANUFACTURER_NUMBER("Manufacturer Number");

    private final String header;

    ExcelField(String header) {
        this.header = header;
    }
}
