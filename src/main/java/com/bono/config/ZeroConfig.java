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

    public String getHost() {
        return properties.getProperty(HOST_PROPERTY);
    }

    public void setHost(String host) {
        properties.setProperty(HOST_PROPERTY, host);
    }

    public int getPort() {
        //System.out.println(properties.getProperty(PORT_PROPERTY));
        return Integer.parseInt(properties.getProperty(PORT_PROPERTY));
    }

    public void setPort(int port) {
        properties.setProperty(PORT_PROPERTY, Integer.toString(port));
    }

    public void setPort(String port) {
        properties.setProperty(PORT_PROPERTY, port);
    }

    public int getSoundcloudResults() {
        return Integer.parseInt(properties.getProperty(SOUNDCLOUD_RESULTS));
    }

    public void setSoundcloudResults(int soundcloudResults) {
        properties.setProperty(SOUNDCLOUD_RESULTS, Integer.toString(soundcloudResults));
    }
}
