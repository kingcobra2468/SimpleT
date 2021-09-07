package com.github.simplet.network.rpist;

public interface RpistTempCallback {
    void onSuccess();

    void onError(String code, String message);
}
