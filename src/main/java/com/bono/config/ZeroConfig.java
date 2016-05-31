package com.bono.config;

import com.bono.api.Config;

/**
 * Created by hendriknieuwenhuis on 31/05/16.
 *
 *  TODO controller / logic voor config!!!
 *
 *  properties populate met Strings.
 *
 *  add listeners en trigger ze als property gezet wordt.
 */
public class ZeroConfig extends Config {

    private final String HOST_PROPERTY = "host_property";
    private final String PORT_PROPERTY = "port_property";
    private final String SOUNDCLOUD_RESULTS = "soundcloud_results";

    public ZeroConfig() {
        super();
    }

    public ZeroConfig(String host, int port) {
        //super(host, port);
        properties.setProperty(HOST_PROPERTY, host);
        properties.setProperty(PORT_PROPERTY, Integer.toString(port));
    }

    public int getSoundcloudResults() {
        return 0;
    }

    public void setSoundcloudResults(int soundcloudResults) {

    }
}
