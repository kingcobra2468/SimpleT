package com.github.simplet.network.clients;

import com.github.simplet.models.rpist.AuthRequest;
import com.github.simplet.models.rpist.AuthResult;
import com.github.simplet.models.rpist.ErrorResult;
import com.github.simplet.models.rpist.TempResult;
import com.github.simplet.network.services.RpistService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RpistNodeClient {
    private String jwt = "";
    private String baseUrl;
    protected RpistService service;

    public RpistNodeClient(String address, int port) {
        baseUrl = String.format("%s:%d", address, port);
        setup();
    }

    public RpistNodeClient(String baseUrl) {
        this.baseUrl = baseUrl;
        setup();
    }

    protected RpistNodeClient setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RpistService.class);

        return this;
    }
    
    public RpistNodeClient connect(String secret) throws IOException {
        Call<AuthResult> call = service.getJWT(new AuthRequest(secret));

        Response<AuthResult> response = call.execute();

        if (!response.isSuccessful()) {
            Gson gson = new Gson();
            ErrorResult error = gson.fromJson(response.errorBody().charStream(), ErrorResult.class);

            throw new RpistErrorException(error.getCode(), error.getMessage());
        }
        jwt = response.body().getJwt();

        return this;
    }

    public void getCelsius(RpistTempCallback callback) {
        Call<TempResult> call = service.getTempCelsius("Bearer " + jwt);

        call.enqueue(new Callback<TempResult>() {
            @Override
            public void onResponse(Call<TempResult> call, Response<TempResult> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    ErrorResult error = gson.fromJson(response.errorBody().charStream(), ErrorResult.class);
                    callback.onError(error.getCode(), error.getMessage());

                    return;
                }

                callback.onSuccess(response.body().getTemperature());
            }

            @Override
            public void onFailure(Call<TempResult> call, Throwable t) {
                callback.onError("Request Failed", t.toString());
            }
        });
    }
}
