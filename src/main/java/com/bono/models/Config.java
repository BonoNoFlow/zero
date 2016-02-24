package com.bono.models;

/**
 * Created by hendriknieuwenhuis on 10/10/15.
 */
public class Config {

    private String host;

    private int port;

    public Config() {}

    public Config(String host) {
        this.host = host;
    }

    public Config(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
