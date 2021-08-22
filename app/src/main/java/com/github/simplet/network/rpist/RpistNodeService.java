package com.github.simplet.network.rpist;

import com.github.simplet.models.rpist.node.AuthRequest;
import com.github.simplet.models.rpist.node.AuthResult;
import com.github.simplet.models.rpist.node.InfoResult;
import com.github.simplet.models.rpist.node.TempResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface RpistNodeService {
    @POST("api/auth")
    Call<AuthResult> getJwt(@Body AuthRequest authData);

    @GET("api/discovery/info")
    Call<InfoResult> getInfo();

    @Headers("Content-Type: application/json")
    @GET("api/temp/get-celsius")
    Call<TempResult> getTempCelsius(@Header("Authorization") String authorization);
}
