package com.medicisupply;

import com.medicisupply.service.ConversionService;

/**
 * Main application class that initializes the conversion service.
 */
public class Main {
    
    public static void main(String[] args) {
        // Create and run the conversion service
        ConversionService conversionService = new ConversionService();
        conversionService.convert(args);
    }
}