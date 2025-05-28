
package com.medicisupply.mapper;

import lombok.Getter;

@Getter
public enum CsvField {
    HANDLE("Handle"),
    COMMAND("Command"),
    TITLE("Title"),
    VENDOR("Vendor"),
    TESTCOLUMN ("TestColumn");

    private final String header;

    CsvField(String header) {
        this.header = header;
    }

}
