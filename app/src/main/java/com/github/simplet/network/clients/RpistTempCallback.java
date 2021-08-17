package com.github.simplet.network.clients;

public interface RpistTempCallback {
    void onSuccess(float temperature);
    void onError(String code, String message);
}
