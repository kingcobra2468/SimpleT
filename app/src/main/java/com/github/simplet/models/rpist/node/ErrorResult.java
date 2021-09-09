package com.github.simplet.models.rpist.node;

/**
 * Json schema for an error result for rpist in node mode.
 */
public class ErrorResult {
    private String code;
    private String message;

    /**
     * Instantiates a new Error result.
     *
     * @param code    the code
     * @param message the message
     */
    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets http code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets error message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets error message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
