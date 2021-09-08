package com.github.simplet.models.rpist.node;

/**
 * Json schema for an auth result for rpist in node mode.
 */
public class AuthResult {
    private String jwt;

    /**
     * Instantiates a new Auth result.
     *
     * @param jwt the jwt
     */
    public AuthResult(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Gets jwt.
     *
     * @return the jwt
     */
    public String getJwt() {
        return jwt;
    }

    /**
     * Sets jwt.
     *
     * @param jwt the jwt
     */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
