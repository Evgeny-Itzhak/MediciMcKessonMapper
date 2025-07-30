package com.medicisupply.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

    public static Map<CsvField, String> getFieldMappings() {
        Map<CsvField, String> map = new LinkedHashMap<>();

        map.put(CsvField.HANDLE, ExcelField.RETAIL_DESCRIPTION.getHeader());
        map.put(CsvField.COMMAND, ExcelField.COMMAND.getHeader());
        map.put(CsvField.TITLE, ExcelField.RETAIL_DESCRIPTION.getHeader());
        map.put(CsvField.BODY_HTML, ExcelField.RETAIL_FEATURES_AND_BENEFITS.getHeader());
        map.put(CsvField.VENDOR, ExcelField.BRAND_OR_SERIES.getHeader());
        map.put(CsvField.TYPE, ExcelField.APPLICATION.getHeader());
        map.put(CsvField.TAGS_COMMAND, ExcelField.APPLICATION.getHeader());
        map.put(CsvField.TAGS, ExcelField.SUPPLY_MANAGER_CATEGORY.getHeader());
        map.put(CsvField.STATUS, ExcelField.APPLICATION.getHeader());
        map.put(CsvField.IMAGE_SRC, ExcelField.IMAGES.getHeader());
        map.put(CsvField.IMAGE_ALT_TEXT, ExcelField.RETAIL_DESCRIPTION.getHeader());
        map.put(CsvField.CUSTOM_COLLECTIONS, ExcelField.SUPPLY_MANAGER_CATEGORY.getHeader());
        map.put(CsvField.VARIANT_SKU, ExcelField.E1_SKU.getHeader());
        map.put(CsvField.VARIANT_WEIGHT, ExcelField.SHIPPING_WEIGHT.getHeader());
        map.put(CsvField.VARIANT_WEIGHT_UNIT, ExcelField.WEIGHT_UOM.getHeader());
        map.put(CsvField.SHIPPING_WIDTH, ExcelField.SHIPPING_WIDTH.getHeader());
        map.put(CsvField.SHIPPING_HEIGHT, ExcelField.SHIPPING_HEIGHT.getHeader());
        map.put(CsvField.SHIPPING_DEPTH, ExcelField.SHIPPING_DEPTH.getHeader());
        map.put(CsvField.SHIPPING_WEIGHT, ExcelField.SHIPPING_WEIGHT.getHeader());
        map.put(CsvField.DIMENSION_UOM, ExcelField.DIMENSION_UOM.getHeader());
        map.put(CsvField.VARIANT_BARCODE, ExcelField.UPC.getHeader());
        map.put(CsvField.VARIANT_IMAGE, ExcelField.PRIMARY_IMAGE.getHeader());
        map.put(CsvField.VARIANT_INVENTORY_TRACKER, "");
        map.put(CsvField.VARIANT_COST, "");
        map.put(CsvField.OPTION1_NAME, ExcelField.SIZE.getHeader());
        map.put(CsvField.OPTION1_VALUE, ExcelField.E1_SKU.getHeader());
        return map;
    }
}