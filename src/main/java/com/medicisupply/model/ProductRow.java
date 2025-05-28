
package com.medicisupply.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ProductRow {
    private final Map<String, String> fields = new HashMap<>();

    public void setField(String key, String value) {
        fields.put(key, value != null ? value.trim() : "");
    }

    public String getField(String key) {
        return fields.getOrDefault(key, "");
    }
}
