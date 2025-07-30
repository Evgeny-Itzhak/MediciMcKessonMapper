package com.medicisupply.mapper;

import lombok.Getter;

/**
 * Defines all the columns read from the input Excel file.
 * Returns the exact header name as it appears in the Excel file.
 */
@Getter
public enum ExcelField {

    //columns that are IN the csv-file
    RETAIL_DESCRIPTION("Retail Description"),
    COMMAND("Command"),
    RETAIL_FEATURES_AND_BENEFITS("Retail Features & Benefits"),
    BRAND_OR_SERIES("Brand or Series"),
    APPLICATION("Application"),
    TAGS_COMMAND ("Tags Command"),
    SUPPLY_MANAGER_CATEGORY("Supply Manager Category"),
    E1_SKU("E1 SKU"),
    SHIPPING_WEIGHT("Shipping Weight"),
    SHIPPING_WIDTH("Shipping Width"),
    SHIPPING_HEIGHT("Shipping Height"),
    SHIPPING_DEPTH("Shipping Depth"),
    WEIGHT_UOM("Weight UOM"),

    //columns that are NOT in csv-file
    MCK_ITEM_NO("McK Item No"),
    MANUFACTURER_NUMBER("Manufacturer Number"),
    PRIMARY_IMAGE("Primary Image"),
    IMAGES("Images");

    private final String header;

    ExcelField(String header) {
        this.header = header;
    }
}