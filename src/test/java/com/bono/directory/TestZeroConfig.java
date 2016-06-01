package com.bono.directory;

import com.bono.config.ZeroConfig;

/**
 * Created by hendriknieuwenhuis on 01/06/16.
 */
public class TestZeroConfig {

    public TestZeroConfig() {

        ZeroConfig config = new ZeroConfig();
        /*
        config.setHost("192.168.2.4");
        config.setPort(6600);
        config.setSoundcloudResults(30);

        try {
            config.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            config.loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String host = config.getHost();
        int port = config.getPort();
        int results = config.getSoundcloudResults();

        System.out.println("Host: " + host + "\n" + "Port: " + port + "\n" + "Results: " + results + "\n");
    }

    public static void main(String[] args) {
        new TestZeroConfig();
    }
}
