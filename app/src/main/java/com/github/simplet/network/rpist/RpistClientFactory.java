package com.github.simplet.network.rpist;

import com.github.simplet.utils.TemperatureScale;

/**
 * Factory for generating different clients based on the mode.
 */
public class RpistClientFactory {

    /**
     * Create client rpist client based on the mode.
     *
     * @param clientType the client mode type
     * @param hostName   the host name of the rpist
     * @param port       the port of the rpist
     * @return the rpist client
     */
    public RpistClient createClient(String clientType, String hostName, int port) {
        if (clientType.equalsIgnoreCase("node")) {
            return new RpistNodeClient(hostName, port);
        } else if (clientType.equalsIgnoreCase("basestation")) {
            return new RpistBaseStationClient(hostName, port);
        } else {
            return null;
        }
    }

    /**
     * Create client rpist client based on the mode.
     *
     * @param clientType   the client mode type
     * @param hostName     the host name of the rpist
     * @param port         the port of the rpist
     * @param defaultScale the default scale for all nodes
     * @return the rpist client
     */
    public RpistClient createClient(String clientType, String hostName, int port,
                                    TemperatureScale defaultScale) {
        if (clientType.equalsIgnoreCase("node")) {
            return new RpistNodeClient(hostName, port, defaultScale);
        } else if (clientType.equalsIgnoreCase("basestation")) {
            return new RpistBaseStationClient(hostName, port, defaultScale);
        } else {
            return null;
        }
    }
}
