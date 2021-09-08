package com.github.simplet.models.rpist.node;

/**
 * Json schema for an temperature result for rpist in node mode. Temperature will always be
 * returned in Celsius.
 */
public class TempResult {
    private float temperature;

    /**
     * Instantiates a new Temp result.
     *
     * @param temperature the temperature
     */
    public TempResult(float temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets temperature of the rpist.
     *
     * @return the temperature
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * Sets temperature.
     *
     * @param temperature the temperature
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
