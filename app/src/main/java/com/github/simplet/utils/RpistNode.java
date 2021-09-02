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

    public float getTemperature() {
        return temperature;
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
