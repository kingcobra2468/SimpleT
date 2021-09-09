package com.github.simplet.models.rpist.node;

/**
 * Json schema for an info result for rpist in node mode.
 */
public class InfoResult {
    private String id;

    /**
     * Instantiates a new Info result.
     *
     * @param id the id
     */
    public InfoResult(String id) {
        this.id = id;
    }

    /**
     * Gets rpist id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets rpist id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }
}
