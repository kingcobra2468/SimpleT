package com.github.simplet.network.rpist;

/**
 * Callback for handling temperature success/failure causes when trying to fetch the
 * temperature.
 */
public interface RpistTempCallback {
    /**
     * Callback for when the temperature is fetched successfully.
     */
    void onSuccess();

    /**
     * Callback for when the temperature is fetched and an exception arises on the rpist end.
     *
     * @param code    the http code returned by the rpist
     * @param message the error message returned from the rpist
     */
    void onError(String code, String message);
}
