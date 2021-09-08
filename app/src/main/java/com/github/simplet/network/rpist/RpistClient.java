package com.github.simplet.network.rpist;

import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Base client for developing different clients under different rpist operation modes.
 */
public abstract class RpistClient {
    /**
     * The jwt used for Api authorization.
     */
    protected String jwt = "";
    /**
     * The secret used for authentication.
     */
    protected String secret;
    /**
     * The base url of the rpist.
     */
    protected String baseUrl;
    /**
     * Connected flag of the client. Indicated whether the connection to the rpist was successful.
     */
    protected boolean connected = false, /**
     * Connection reset flag. Triggered when preferences changes.
     */
    connectionReset = true;
    /**
     * The default temperature scale to use for all of the nodes.
     */
    protected TemperatureScale scale = TemperatureScale.CELSIUS;
    /**
     * The rpist node(s) representing the data returned by the rpist.
     */
    protected LinkedHashMap<String, RpistNode> rpists;

    /**
     * Instantiates a new rpist client.
     *
     * @param address the address of the rpist
     * @param port    the port of the rpist
     */
    public RpistClient(String address, int port) {
        baseUrl = String.format("%s:%d", address, port);
        rpists = new LinkedHashMap<>();
    }

    /**
     * Instantiates a new rpist client.
     *
     * @param address      the address of the rpist
     * @param port         the port of the rpist
     * @param defaultScale the default scale for each node
     */
    public RpistClient(String address, int port, TemperatureScale defaultScale) {
        baseUrl = String.format("%s:%d", address, port);
        rpists = new LinkedHashMap<>();
        scale = defaultScale;
    }

    /**
     * Instantiates a new rpist client.
     *
     * @param baseUrl the base url
     */
    public RpistClient(String baseUrl) {
        this.baseUrl = baseUrl;
        rpists = new LinkedHashMap<>();
    }

    /**
     * Connect rpist client by validating that the host is up. Build the internal retrofit object,
     *
     * @param secret the secret used for authentication
     * @return the rpist client
     * @throws IOException the io exception on connection related issues
     */
    public RpistClient connect(String secret) throws IOException {
        this.secret = secret;
        setup();

        return this;
    }

    /**
     * Check if the connection to the rpist is currently valid.
     *
     * @return the stage of the boolean flag
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Reset the existing connection by setting an internal flag.
     */
    public void resetConnection() {
        connectionReset = true;
    }

    /**
     * Check if the connection to the rpist is reset.
     *
     * @return the stage of the boolean flag
     */
    public boolean isConnectionReset() {
        return connectionReset;
    }

    /**
     * Sets base url of the rpist.
     *
     * @param baseUrl the base url of the rpist
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Sets base url of the rpist.
     *
     * @param address the address of the rpist
     * @param port    the port of the rpist
     */
    public void setBaseUrl(String address, int port) {
        baseUrl = String.format("%s:%d/", address, port);
    }

    /**
     * Perform any necessary setup of the rpist. Depends on the rpist mode.
     *
     * @return the instance of the rpist client
     */
    protected abstract RpistClient setup();

    /**
     * Gets the node presentations of the data returned by the rpist.
     *
     * @return the rpist nodes
     */
    public abstract List<RpistNode> getRpistNodes();

    /**
     * Sets temperature scale for a specific node.
     *
     * @param rpistId the rpist id belonging to the node which will have its scale changed
     * @param scale   the scale to be changed to
     */
    public abstract void setNodeScale(String rpistId, TemperatureScale scale);

    /**
     * Fetch rpist id belonging to the rpist node.
     *
     * @return the rpist client
     * @throws IOException the io exception
     */
    public abstract RpistClient fetchRpistId() throws IOException;

    /**
     * Gets Celsius temperature data from the rpist.
     *
     * @param callback the callback
     */
    public abstract void getCelsius(RpistTempCallback callback);
}
