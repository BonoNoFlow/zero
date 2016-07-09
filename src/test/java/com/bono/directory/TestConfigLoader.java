package com.bono.directory;

import com.bono.Application;
import com.bono.ConfigLoader;
import com.bono.api.Endpoint;
import com.bono.view.ConnectionDialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */
public class TestConfigLoader {

    static final String HOST = "HOST";
    static final String PORT = "PORT";

    static String[] hosts = {"192.168.2.1", "192.168.2.2", "192.168.2.3", "192.168.2.4"};

    static Endpoint endpoint = null;

    static String version;

    static List<String> config = null;

    static Object lock = new Object();

    static boolean loading = true;

    static boolean showing;

    static void testEndpoint() throws Exception {

        // does dir exists? no.., create dir.
        if (!ConfigLoader.isDirectoryPresent()) {
            ConfigLoader.createSyncDir();
        }

        /*
        Dit werkt nu als:

        Laden is er een bestand.
         zo niet dan vragen naar gegevens.
         loop opnieuw beginnen.

         er is wel een bestand.
        Controleren van gegevens.
         niet correct gegevens wissen,
         loop opnieuw beginnen.
         */
        while (loading) {

            // file wordt geladen. Bij geen file
            // NoSuchFileException wordt geinitieerd
            // en Dialoog frame wordt getoond met
            // juiste boodschap.
            // TODO jdialog starten via method met arg String message. In catch!
            if (config == null) {
                try {
                    config = ConfigLoader.readConnectionConfig();
                } catch (NoSuchFileException nsf) {

                    // no file so ask for host and port.
                    // open config dialog.
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
                            // TODO. als file niet geschreven kan worden? ....
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

            } else {

                // eerst checken of geladen waarden kloppen.
                // als waarden NIET kloppen opnieuw waarden vragen!

                if (testConnection()) {

                    showing = true;
                    ConnectionDialog connectionDialog = new ConnectionDialog(Application.screenDimension());
                    connectionDialog.setMessage("verkeerde waarden");
                    config = null;

                    // save button listener.
                    connectionDialog.addSaveActionListener((event) -> {
                        String host = "HOST " + connectionDialog.getConfigConnectionView().getHostField();
                        String port = "PORT " + connectionDialog.getConfigConnectionView().getPortField();
                        List<String> list = Arrays.asList(host, port);
                        try {
                            ConfigLoader.writeConnectionConfig(list);
                        } catch (IOException e) {

                            // TODO. als file niet geschreven kan worden? ....
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
                } else {

                    // waarden kloppen uit laad modes gaan!
                    loading = false;
                }
            }
        }
    }




    static boolean testConnection() {
        String host = null;
        String port = null;

        if (config == null) {
            return true;
        }

        for (String c : config) {
            String[] param = c.split(" ");
            switch (param[0]) {
                case HOST:
                    if (param.length == 2) {
                        host = param[1];
                    } else {
                        host = null;
                    }
                    break;
                case PORT:
                    if (param.length == 2) {
                        port = param[1];
                    } else {
                        port = null;
                    }
                    break;
                default:
                    break;
            }
        }

        // test the connection settings.
        if (host != null && port != null) {
            endpoint = new Endpoint(host, Integer.parseInt(port));
            try {
                String version = endpoint.getVersion(1000);
                System.out.println(version);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return true;
            }
            return false;
        }

        return true;

    }



    public static void main(String[] args) {

        //TestConfigLoader testConfigLoader = new TestConfigLoader();
        ConfigLoader configLoader = new ConfigLoader();
        try {
            configLoader.testEndpoint();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
