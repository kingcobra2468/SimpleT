package com.github.simplet.models.rpist.node;

public class AuthResult {
    private String jwt;

    public AuthResult(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
