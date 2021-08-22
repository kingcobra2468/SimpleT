package com.github.simplet.network.rpist;

public class RpistErrorException extends java.io.IOException {
    public RpistErrorException(String code, String message) {
        super(String.format("%s - %s", code, message));
    }
}
