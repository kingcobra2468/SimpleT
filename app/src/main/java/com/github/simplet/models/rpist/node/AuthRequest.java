package com.github.simplet.models.rpist.node;

/**
 * Json schema for an auth request for rpist in node mode.
 */
public class AuthRequest {
    private String secret;

    /**
     * Instantiates a new Auth request.
     *
     * @param secret the secret
     */
    public AuthRequest(String secret) {
        this.secret = secret;
    }

    /**
     * Gets secret.
     *
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets secret.
     *
     * @param secret the secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
