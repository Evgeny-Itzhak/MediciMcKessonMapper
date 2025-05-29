package com.medicisupply.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ProductRow {
    private final Map<String, String> fields = new HashMap<>();

    public ProductRow() {
        // default constructor for manual field setup
    }

    public ProductRow(Map<String, String> fields) {
        this.fields.putAll(fields);
    }

    public void setField(String key, String value) {
        this.fields.put(key, value);
    }

    public String getField(String key) {
        return this.fields.getOrDefault(key, "");
    }
}
