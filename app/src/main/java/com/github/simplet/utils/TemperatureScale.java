package com.github.simplet.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TemperatureScale {
    CELSIUS("C"),
    FAHRENHEIT("F"),
    KELVIN("K");

    public static List<TemperatureScale> scales =
            new ArrayList<>(Arrays.asList(TemperatureScale.values()));
    public final String symbol;

    TemperatureScale(String symbol) {
        this.symbol = symbol;
    }
}
