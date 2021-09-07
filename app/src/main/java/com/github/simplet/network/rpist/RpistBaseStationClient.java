package com.github.simplet.network.rpist;

import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: implement once base station microservice is implemented. For now call setFakeNodes
public class RpistBaseStationClient extends RpistClient {

    public RpistBaseStationClient(String address, int port) {
        super(address, port);
        setFakeNodes();
    }

    public RpistBaseStationClient(String address, int port, TemperatureScale defaultScale) {
        super(address, port, defaultScale);
        setFakeNodes();
    }

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

    private void setFakeNodes() {
        rpists.putIfAbsent("Room 1", new RpistNode(70, scale, "Room 1"));
        rpists.putIfAbsent("Room 2", new RpistNode(40, scale, "Room 2"));
        rpists.putIfAbsent("Room 3", new RpistNode(50, scale, "Room 3"));

        connected = true;
        connectionReset = false;
    }

}
