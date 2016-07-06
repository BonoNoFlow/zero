package com.bono.directory;

import com.bono.Application;
import com.bono.ConfigLoader;
import com.bono.api.Endpoint;
import com.bono.view.ConnectionDialog;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */
public class TestConfigLoader {

    static String[] hosts = {"192.168.2.1", "192.168.2.2", "192.168.2.3", "192.168.2.4"};

    static Endpoint endpoint = null;

    static String version;

    static List<String> config = null;

    static Object lock = new Object();

    static boolean loading = true;

    static boolean showing;

    static void testEndpoint() throws Exception {

        /*
        Dit werkt config in laden en
        kijken of er een bestand is!

        TODO config parameters controleren op werkend!!!
         */



        while (loading) {
            System.out.println("statrt");
            try {
                config = ConfigLoader.readConnectionConfig();
            } catch (NoSuchFileException nsf) {
                // no file so ask for host and port.
                //  open config dialog.
                showing = true;
                ConnectionDialog connectionDialog = new ConnectionDialog(Application.screenDimension());

                // save button listener.
                connectionDialog.addSaveActionListener((event) -> {
                    String host = "HOST " + connectionDialog.getConfigConnectionView().getHostField();
                    String port = "PORT " + connectionDialog.getConfigConnectionView().getPortField();
                    List<String> list = Arrays.asList(host, port);
                    try {
                        ConfigLoader.writeConnectionConfig(list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // closes the jdialog.
                    connectionDialog.dispose();
                    synchronized (lock) {
                        showing = false;
                        lock.notify();
                    }
                });

                connectionDialog.showDialog();
                synchronized (lock) {
                    while (showing) {
                        try {
                            lock.wait();
                        } catch (InterruptedException i) {
                            i.printStackTrace();
                        }
                    }
                }
                continue;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            System.out.println("Test params");
            loading = false;
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
