package com.github.simplet.network.rpist;

/**
 * Exception thrown for all rpist io related errors.
 */
public class RpistErrorException extends java.io.IOException {
    /**
     * Instantiates a new rpist error exception.
     *
     * @param code    the http code
     * @param message the error message from the rpist
     */
    public RpistErrorException(String code, String message) {
        super(String.format("%s - %s", code, message));
    }
}
