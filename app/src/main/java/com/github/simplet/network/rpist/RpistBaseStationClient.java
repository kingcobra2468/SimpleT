package com.github.simplet.network.rpist;

import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for when the rpist is operated as a base station.
 */
// TODO: implement once base station microservice is implemented. For now call setFakeNodes
public class RpistBaseStationClient extends RpistClient {

    /**
     * Instantiates a new rpist base station client.
     *
     * @param address the address of the rpist
     * @param port    the port of the rpist
     */
    public RpistBaseStationClient(String address, int port) {
        super(address, port);
        setFakeNodes();
    }

    /**
     * Instantiates a new rpist base station client.
     *
     * @param address      the address of the rpist
     * @param port         the port of the rpist
     * @param defaultScale the default temperature scale for the nodes
     */
    public RpistBaseStationClient(String address, int port, TemperatureScale defaultScale) {
        super(address, port, defaultScale);
        setFakeNodes();
    }

    /**
     * Instantiates a new rpist base station client.
     *
     * @param baseUrl the base url of the rpist
     */
    public RpistBaseStationClient(String baseUrl) {
        super(baseUrl);

        setFakeNodes();
    }

    @Override
    protected RpistClient setup() {
        return null;
    }

    @Override
    public List<RpistNode> getRpistNodes() {

        return new ArrayList<>(rpists.values());
    }

    @Override
    public void setNodeScale(String rpistId, TemperatureScale scale) {

    }

    @Override
    public RpistClient fetchRpistId() throws IOException {
        return this;
    }

    @Override
    public void getCelsius(RpistTempCallback callback) {
        // psuedo temperatures
        rpists.forEach((key, value) -> {
            rpists.get(key).setTemperature((float) Math.random() * (100 - 0 + 1) + 5);
        });

        callback.onSuccess();
    }

    /**
     * Generate fake data for when node is operated in base station mode until the client
     * is fully implemented.
     */
    private void setFakeNodes() {
        rpists.putIfAbsent("Room 1", new RpistNode(70, scale, "Room 1"));
        rpists.putIfAbsent("Room 2", new RpistNode(40, scale, "Room 2"));
        rpists.putIfAbsent("Room 3", new RpistNode(50, scale, "Room 3"));

        connected = true;
        connectionReset = false;
    }

}
