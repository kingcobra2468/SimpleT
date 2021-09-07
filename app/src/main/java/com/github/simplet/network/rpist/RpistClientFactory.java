package com.github.simplet.network.rpist;

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
}
