package com.github.simplet.models.rpist.node;

public class AuthRequest {
    private String secret;

    public AuthRequest(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
