package com.github.simplet.network.rpist;

import com.github.simplet.utils.TemperatureScale;

public class RpistClientFactory {

    public RpistClient createClient(String clientType, String hostName, int port) {
        if (clientType.equalsIgnoreCase("node")) {
            return new RpistNodeClient(hostName, port);
        } else if (clientType.equalsIgnoreCase("basestation")) {
            return new RpistBaseStationClient(hostName, port);
        } else {
            return null;
        }
    }

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
