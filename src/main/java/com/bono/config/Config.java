package com.bono.config;

import java.io.*;
import java.util.Properties;

/**
 * Created by hendriknieuwenhuis on 17/02/16.
 */
@Deprecated
public class Config {

    public static final String CONFIG_FILE = "server.properties";
    public static final String HOST_PROPERTY = "host_property";
    public static final String PORT_PROPERTY = "port_property";
    public static final String SOUNDCLOUD_RESULTS = "soundcloud_results";

    private Properties properties;

    public Config() {
        properties = new Properties();
    }

    public void loadConfig() throws Exception {
        //properties = new Properties();
        InputStream inputStream = null;

        // First try loading from the current directory.
        try {
            File file = new File(Config.CONFIG_FILE);
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            inputStream = null;
        }

        /* here's where the exception throw comes from. */
        if (inputStream == null) {
            // try loading from classpath.
            inputStream = getClass().getResourceAsStream(Config.CONFIG_FILE);
        }
        // try loading properties from the file (if found)
        properties.load(inputStream);
    }

    public void saveConfig() throws Exception {
        try {
            File file = new File(Config.CONFIG_FILE);
            OutputStream out = new FileOutputStream(file);
            properties.store(out, "properties");
        } catch (Exception e) {
            throw e;
        }
    }

    public void setProperty(String key, String property) {
        properties.setProperty(key, property);
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
