package com.github.simplet.network.services;

import com.github.simplet.models.rpist.AuthRequest;
import com.github.simplet.models.rpist.AuthResult;
import com.github.simplet.models.rpist.TempResult;

import retrofit2.http.*;
import retrofit2.*;

public interface RpistService {
    @POST("api/auth")
    Call<AuthResult> getJWT(@Body AuthRequest authData);

    @Headers("Content-Type: application/json")
    @GET("api/temp/get-celsius")
    Call<TempResult> getTempCelsius(@Header("Authorization") String authorization);
}
