package com.github.simplet.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The enum to represent different temperature scales.
 */
public enum TemperatureScale {
    /**
     * Celsius temperature scale.
     */
    CELSIUS("C"),
    /**
     * Fahrenheit temperature scale.
     */
    FAHRENHEIT("F"),
    /**
     * Kelvin temperature scale.
     */
    KELVIN("K");

    /**
     * The available list of scales.
     */
    public static List<TemperatureScale> scales =
            new ArrayList<>(Arrays.asList(TemperatureScale.values()));
    /**
     * The symbol for the set temeprature scale.
     */
    public final String symbol;

    TemperatureScale(String symbol) {
        this.symbol = symbol;
    }
}
