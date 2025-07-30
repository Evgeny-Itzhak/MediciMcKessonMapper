
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
    IMAGE_SRC("Image Src");

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