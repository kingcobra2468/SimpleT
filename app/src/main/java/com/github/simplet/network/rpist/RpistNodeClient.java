package com.github.simplet.network.rpist;

import com.github.simplet.models.rpist.node.AuthRequest;
import com.github.simplet.models.rpist.node.AuthResult;
import com.github.simplet.models.rpist.node.ErrorResult;
import com.github.simplet.models.rpist.node.InfoResult;
import com.github.simplet.models.rpist.node.TempResult;
import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RpistNodeClient extends RpistClient {
    protected String rpistId;

    public RpistNodeClient(String address, int port) {
        super(address, port);
    }

    public RpistNodeClient(String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected RpistClient setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RpistNodeService.class);

        return this;
    }

    @Override
    public List<RpistNode> getRpistNodes() {
        return new ArrayList<>(rpists.values());
    }

    @Override
    public void setNodeScale(String rpistId, TemperatureScale scale) {
    }

    @Override
    public RpistClient connect(String secret) throws IOException {
        super.connect(secret);

        Call<AuthResult> call = service.getJwt(new AuthRequest(this.secret));

        Response<AuthResult> response = call.execute();

        if (!response.isSuccessful()) {
            Gson gson = new Gson();
            ErrorResult error = gson.fromJson(response.errorBody().charStream(), ErrorResult.class);

            throw new RpistErrorException(error.getCode(), error.getMessage());
        }
        jwt = response.body().getJwt();
        connected = true;
        connectionReset = false;

        return this;
    }

    public RpistClient fetchRpistId() throws IOException {
        Call<InfoResult> call = service.getInfo();

        Response<InfoResult> response = call.execute();

        if (!response.isSuccessful()) {
            Gson gson = new Gson();
            ErrorResult error = gson.fromJson(response.errorBody().charStream(), ErrorResult.class);

            throw new RpistErrorException(error.getCode(), error.getMessage());
        }
        rpistId = response.body().getId();

        rpists.clear();
        rpists.putIfAbsent(rpistId, new RpistNode().setId(rpistId));

        return this;
    }

    public void getCelsius(RpistTempCallback callback) {
        Call<TempResult> call = service.getTempCelsius("Bearer " + jwt);

        call.enqueue(new Callback<TempResult>() {
            @Override
            public void onResponse(Call<TempResult> call, Response<TempResult> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    ErrorResult error = gson.fromJson(response.errorBody().charStream(),
                            ErrorResult.class);
                    callback.onError(error.getCode(), error.getMessage());

                    return;
                }
                float temperature = response.body().getTemperature();
                RpistNode node = rpists.getOrDefault(rpistId, new RpistNode().setId(rpistId));
                rpists.putIfAbsent(rpistId, node);
                node.setTemperature(temperature);

                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<TempResult> call, Throwable t) {
                callback.onError("Request Failed", t.toString());
            }
        });
    }
}
