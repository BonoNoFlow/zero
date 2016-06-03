package com.bono.directory;

import com.bono.config.ZeroConfig;

/**
 * Created by hendriknieuwenhuis on 01/06/16.
 */
public class TestZeroConfig {

    public TestZeroConfig() {

        ZeroConfig config = new ZeroConfig();

        config.setProperty(ZeroConfig.HOST_PROPERTY, "192.168.2.4");
        config.setProperty(ZeroConfig.PORT_PROPERTY, "6600");
        config.setProperty(ZeroConfig.SOUNDCLOUD_RESULTS, "30");

        try {
            config.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            config.loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String host = config.getProperty(ZeroConfig.HOST_PROPERTY);
        int port = Integer.parseInt(config.getProperty(ZeroConfig.PORT_PROPERTY));
        int results = Integer.parseInt(config.getProperty(ZeroConfig.SOUNDCLOUD_RESULTS));

        System.out.println("Host: " + host + "\n" + "Port: " + port + "\n" + "Results: " + results + "\n");
    }

    public static void main(String[] args) {
        new TestZeroConfig();
    }
}
