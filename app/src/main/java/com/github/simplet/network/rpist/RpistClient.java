package com.github.simplet.network.rpist;

import com.github.simplet.models.rpist.node.AuthRequest;
import com.github.simplet.models.rpist.node.AuthResult;
import com.github.simplet.models.rpist.node.ErrorResult;
import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public abstract class RpistClient {
    protected String jwt = "";
    protected String secret;
    protected String baseUrl;
    protected boolean connected = false, connectionReset = true;
    protected RpistNodeService service;
    protected LinkedHashMap<String, RpistNode> rpists;

    public RpistClient(String address, int port) {
        baseUrl = String.format("%s:%d", address, port);
        rpists = new LinkedHashMap<>();
    }

    public RpistClient(String baseUrl) {
        this.baseUrl = baseUrl;
        rpists = new LinkedHashMap<>();
    }

    public RpistClient connect(String secret) throws IOException {
        this.secret = secret;
        setup();

        return this;
    }

    public boolean isConnected() {
        return connected;
    }

    public void resetConnection() {
        connectionReset = true;
    }

    public boolean isConnectionReset() {
        return connectionReset;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBaseUrl(String address, int port) {
        baseUrl = String.format("%s:%d/", address, port);
    }

    protected abstract RpistClient setup();

    public abstract List<RpistNode> getRpistNodes();

    public abstract void setNodeScale(String rpistId, TemperatureScale scale);
}
