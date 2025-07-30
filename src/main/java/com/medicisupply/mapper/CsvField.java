
package com.medicisupply.mapper;

import lombok.Getter;

@Getter
public enum CsvField {
    HANDLE("Handle"),
    COMMAND("Command", "MERGE"),
    TITLE("Title"),
    BODY_HTML("Body HTML"),
    VENDOR("Vendor"),
    TYPE ("Type"),
    TAGS_COMMAND ("Tags Command", "REPLACE"),
    TAGS("Tags"),
    STATUS("Status", "Active"),
    IMAGE_SRC("Image Src"),
    IMAGE_ALT_TEXT("Image Alt Text"),
    CUSTOM_COLLECTIONS("Custom Collections"),
    VARIANT_SKU("Variant SKU"),
    VARIANT_WEIGHT("Variant Weight"),
    VARIANT_WEIGHT_UNIT("Variant Weight Unit"),
    SHIPPING_WIDTH("Shipping Width"),
    SHIPPING_HEIGHT("Shipping Height"),
    SHIPPING_DEPTH("Shipping Depth"),
    SHIPPING_WEIGHT("Shipping Weight"),
    DIMENSION_UOM("Dimension UOM"),
    VARIANT_BARCODE("Variant Barcode"),
    VARIANT_IMAGE("Variant Image"),
    VARIANT_INVENTORY_TRACKER("Variant Inventory Tracker"),
    VARIANT_COST("Variant Cost"),
    OPTION1_NAME("Option1 Name"),
    OPTION1_VALUE("Option1 Value"),
    OPTION2_NAME("Option2 Name"),
    OPTION2_VALUE("Option2 Value"),
    OPTION3_NAME("Option3 Name"),
    OPTION3_VALUE("Option3 Value"),
    OPTION4_NAME("Option4 Name"),
    OPTION4_VALUE("Option4 Value");

    private final String header;
    private final String defaultValue;

    CsvField(String header) {
        this(header, null);
    }

    CsvField(String header, String defaultValue) {
        this.header = header;
        this.defaultValue = defaultValue;
    }
}