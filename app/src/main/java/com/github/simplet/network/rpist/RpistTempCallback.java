package com.github.simplet.network.rpist;

public interface RpistTempCallback {
    void onSuccess(float temperature);

    void onError(String code, String message);
}
