package com.bono.config;

import java.io.*;
import java.util.Properties;

/**
 * Created by hendriknieuwenhuis on 17/02/16.
 */
public class Config {

    private String host;
    private int port;

    public Config() {}

    public Config(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // moet exception gooien!
    public void loadParams() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = null;

        // First try loading from the current directory.
        try {
            File file = new File("server.properties");
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            inputStream = null;
        }

        try {
            if (inputStream == null) {
                // try loading from classpath.
                inputStream = getClass().getResourceAsStream("server.properties");
            }

            // try loading properties from the file (if found)
            properties.load(inputStream);
        } catch (Exception e) {
            // ask user for params.
            //properties.setProperty("HostAddress", "192.168.2.4");
            //properties.setProperty("Port", "6600");

            throw new Exception();
            //ConfigOptions configOptions = new ConfigOptions(this);
            //loadParams();
            //return;
        }

        host = properties.getProperty("HostAddress");
        port = new Integer(properties.getProperty("Port"));

    }

    public void saveParamChanges() {
        try {
            Properties properties = new Properties();
            properties.setProperty("HostAddress", host);
            properties.setProperty("Port", Integer.toString(port));
            File file = new File("server.properties");
            OutputStream out = new FileOutputStream(file);
            properties.store(out, "properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
