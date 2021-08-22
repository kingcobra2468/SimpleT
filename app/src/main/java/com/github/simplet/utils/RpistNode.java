package com.github.simplet.utils;

public class RpistNode {
    private float mTemperature;
    private TemperatureScale mTemperatureScale;

    public RpistNode() {
        mTemperature = -99;
        mTemperatureScale = TemperatureScale.CELSIUS;
    }

    public RpistNode(float temperature, TemperatureScale scale) {
        mTemperature = temperature;
        mTemperatureScale = scale;
    }

    public float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(float temperature) {
        this.mTemperature = temperature;
    }

    public TemperatureScale getTemperatureScale() {
        return mTemperatureScale;
    }

    public void setTemperatureScale(TemperatureScale temperatureScale) {
        this.mTemperatureScale = temperatureScale;
    }
}
