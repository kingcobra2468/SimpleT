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
    protected String baseUrl;
    protected RpistNodeService service;
    protected LinkedHashMap<String, RpistNode> rpists;

    public RpistClient(String address, int port) {
        baseUrl = String.format("%s:%d", address, port);
        rpists = new LinkedHashMap<>();
        setup();
    }

    public RpistClient(String baseUrl) {
        this.baseUrl = baseUrl;
        rpists = new LinkedHashMap<>();
        setup();
    }

    public RpistClient connect(String secret) throws IOException {
        Call<AuthResult> call = service.getJwt(new AuthRequest(secret));

        Response<AuthResult> response = call.execute();

        if (!response.isSuccessful()) {
            Gson gson = new Gson();
            ErrorResult error = gson.fromJson(response.errorBody().charStream(), ErrorResult.class);

            throw new RpistErrorException(error.getCode(), error.getMessage());
        }
        jwt = response.body().getJwt();

        return this;
    }

    protected abstract RpistClient setup();

    public abstract List<RpistNode> getRpistNodes();

    public abstract void setNodeScale(String rpistId, TemperatureScale scale);
}
