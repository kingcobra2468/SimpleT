package com.github.simplet.utils;

/**
 * Node designated to represent a rpist node from the data returned by the
 * rpist node/base station.
 */
public class RpistNode {
    private float temperature;
    private TemperatureScale temperatureScale;
    private String id;

    /**
     * Instantiates a new rpist node.
     */
    public RpistNode() {
        temperature = -99;
        temperatureScale = TemperatureScale.CELSIUS;
    }

    /**
     * Instantiates a new rpist node.
     *
     * @param temperature the temperature of the rpist node
     * @param scale       the temperature scale of the rpist node
     * @param id          the rpist id of the rpist node
     */
    public RpistNode(float temperature, TemperatureScale scale, String id) {
        this.temperature = temperature;
        this.id = id;
        temperatureScale = scale;
    }

    /**
     * Gets temperature value based on the current set temperature scale.
     *
     * @return the temperature
     */
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

    /**
     * Sets temperature for the rpist node.
     *
     * @param temperature the temperature
     * @return the temperature
     */
    public RpistNode setTemperature(float temperature) {
        this.temperature = temperature;

        return this;
    }

    /**
     * Gets rpsit id of the rpist node.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets rpist id of the node.
     *
     * @param id the id
     * @return the id
     */
    public RpistNode setId(String id) {
        this.id = id;

        return this;
    }

    /**
     * Gets the currently set temperature scale.
     *
     * @return the temperature scale
     */
    public TemperatureScale getTemperatureScale() {
        return temperatureScale;
    }

    /**
     * Sets new temperature scale for the node.
     *
     * @param temperatureScale the temperature scale
     * @return the temperature scale
     */
    public RpistNode setTemperatureScale(TemperatureScale temperatureScale) {
        this.temperatureScale = temperatureScale;

        return this;
    }
}
