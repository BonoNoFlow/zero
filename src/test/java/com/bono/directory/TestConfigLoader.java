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

        /*
        Dit werkt config in laden en
        kijken of er een bestand is!

        TODO config parameters controleren op werkend!!!
         */

        //int a = 0;

        // TODO. while loop moet weg dit komt maar 1 keer voor. Hierna is er altijd een file.
        // todo. tenzij file niet geschreven kan worden!
        while (loading) {
            //System.out.println("statrt " + a++);

            // de eerste keer probeer file te laden maar lukt niet
            // want er is geen file.
            // NoSuchFileException is geroepen en het dialoog verscijnt
            // de gebruiker vult gegevens in. Dialoog wordt gesloten en
            // gegevens worden geschreven.
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
            loading = testConnection();
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
                    System.out.println(param.length);
                    if (param.length != 1) host = param[1];
                    break;
                case PORT:
                    if (param.length != 1) port = param[1];
                    break;
                default:
                    break;
            }
        }

        if (host != null && port != null) {
            return false;
        }
        return true;
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
