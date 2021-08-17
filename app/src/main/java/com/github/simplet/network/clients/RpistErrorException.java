package com.github.simplet.network.clients;

public class RpistErrorException extends java.io.IOException {
    public RpistErrorException(String code, String message) {
        super(String.format("%s - %s", code, message));
    }
}
