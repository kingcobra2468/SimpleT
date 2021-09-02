package com.github.simplet.utils;

public class RpistNode {
    private float temperature;
    private TemperatureScale temperatureScale;

    public RpistNode() {
        temperature = -99;
        temperatureScale = TemperatureScale.CELSIUS;
    }

    public RpistNode(float temperature, TemperatureScale scale) {
        this.temperature = temperature;
        temperatureScale = scale;
    }

    public double getTemperature() {
        double adaptedTemperature = temperature;

        switch (temperatureScale) {
            case CELSIUS:
                break;
            case FAHRENHEIT:
                adaptedTemperature = (temperature * (9.0 / 5.0)) + 32.0;
                break;
            case KELVIN:
                adaptedTemperature = temperature + 273.15;
                break;
        }

        return adaptedTemperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public TemperatureScale getTemperatureScale() {
        return temperatureScale;
    }

    public void setTemperatureScale(TemperatureScale temperatureScale) {
        this.temperatureScale = temperatureScale;
    }
}
