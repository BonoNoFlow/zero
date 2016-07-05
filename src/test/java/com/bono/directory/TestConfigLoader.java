package com.bono.directory;

import com.bono.ConfigLoader;
import com.bono.api.Endpoint;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.file.NoSuchFileException;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */
public class TestConfigLoader {

    static String[] hosts = {"192.168.2.1", "192.168.2.2", "192.168.2.3", "192.168.2.4"};

    static Endpoint endpoint = null;

    static String version;

    static List<String> config = null;

    static void testEndpoint() throws Exception {

        try {
            config = ConfigLoader.readConnectionConfig();
        } catch (NoSuchFileException nsf) {
            System.out.println("No file!");
            //System.exit(1);
            // TODO open config dialog.
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }




        /*

        //endpoint = new Endpoint("192.168.2.5", 6600);
        for (int x = 0; x < hosts.length; x++) {
            //System.out.println(hosts[x]);

            endpoint = new Endpoint(hosts[x], 6600);
            try {
                version = endpoint.getVersion(4000);
            } catch (SocketTimeoutException s) {
                //System.out.println("Next!");
                continue;
            } catch (ConnectException c) {
                //System.out.println(c.getMessage());
                continue;
            }
            System.out.println("Version: " + version + "Attempt: " + x);
        }*/
        /*
        try {
            endpoint.getVersion(4000);
        } catch (SocketTimeoutException s) {
            System.out.println("Next!");
            System.exit(1);
        }*/
    }

    static void wrongConfigInformation() {

    }

    public static void main(String[] args) {

        TestConfigLoader testConfigLoader = new TestConfigLoader();
        try {
            testEndpoint();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        List<String> conf = Arrays.asList(
                "HOST 192.168.2.4",
                "PORT 6600"
        );

        try {
            //ConfigLoader.createSyncDir();
            //ConfigLoader.createIndexFile();
            ConfigLoader.writeConnectionConfig(conf);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }*/
    }
}
