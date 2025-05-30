
package com.medicisupply;

import com.medicisupply.config.AppConfig;
import com.medicisupply.config.AppConfigLoader;
import com.medicisupply.converter.XlsxToCsvConverter;

public class Main {
    public static void main(String[] args) {
        AppConfig config = AppConfigLoader.load(args);
        new XlsxToCsvConverter(config).convert();
    }
}