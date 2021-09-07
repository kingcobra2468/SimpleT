package com.github.simplet.utils;

public class RpistNode {
    private float temperature;
    private TemperatureScale temperatureScale;
    private String id;

    public RpistNode() {
        temperature = -99;
        temperatureScale = TemperatureScale.CELSIUS;
    }

    public RpistNode(float temperature, TemperatureScale scale, String id) {
        this.temperature = temperature;
        this.id = id;
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

    public RpistNode setTemperature(float temperature) {
        this.temperature = temperature;

        return this;
    }

    public String getId() {
        return id;
    }

    public RpistNode setId(String id) {
        this.id = id;

        return this;
    }

    public TemperatureScale getTemperatureScale() {
        return temperatureScale;
    }

    public RpistNode setTemperatureScale(TemperatureScale temperatureScale) {
        this.temperatureScale = temperatureScale;

        return this;
    }
}
