
package com.medicisupply.mapper;

import com.medicisupply.model.ProductRow;

import static com.medicisupply.mapper.CsvField.COMMAND;

public class FieldProcessor {
    public static String process(CsvField field, ProductRow row) {
        switch (field) {
            case HANDLE:
                return row.getField("E1 SKU");
            case TITLE:
                return row.getField("Retail Description");
            case VENDOR:
                return row.getField("Brand or Series");
            case COMMAND:
                return COMMAND.getDefaultValue();
            default:
                return row.getField(field.getHeader());
        }
    }
}
