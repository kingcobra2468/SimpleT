package com.github.simplet.utils;

public enum TemperatureScale {
    CELSIUS("C"),
    FAHRENHEIT("F"),
    KELVIN("K");

    public final String symbol;

    TemperatureScale(String symbol) {
        this.symbol = symbol;
    }
}
