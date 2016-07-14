package com.bono;

import com.bono.api.Config;
import com.bono.api.Endpoint;
import com.bono.view.ConnectionDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */

// Todo file kan niet geschreven worden.
public class ConfigLoader {

    private static final Path DIR = Paths.get(System.getProperty("user.home") + "/.zero");
    private static final Path TDIR = Paths.get(".zero");
    private static final Path FILE = Paths.get(DIR + "/config.file");
    private static final Path TFILE = Paths.get(TDIR + "/config.file");

    private static ConnectionDialog connectionDialog;

    private static final String HOST = "HOST";
    private static final String PORT = "PORT";
    private static final String VERSION = "VERSION";

    private static String[] hosts = {"192.168.2.1", "192.168.2.2", "192.168.2.3", "192.168.2.4"};

    private static Endpoint endpoint = null;

    private static String version;

    private static List<String> config = null;

    private static Object lock = new Object();

    private static boolean loading = true;

    private static boolean showing;

    public ConfigLoader() {
        super();
    }

    /*
    public void testEndpoint() throws Exception {

        // does dir exists? no.., create dir.
        if (!ConfigLoader.isDirectoryPresent()) {
            ConfigLoader.createSyncDir();
        }

        while (loading) {

            if (config == null) {
                // There's no config file.
                // ask for connection settings
                // host and port, restart the loop.
                try {
                    config = ConfigLoader.readConnectionConfig();
                } catch (NoSuchFileException nsf) {
                    showDialog("Please provide settings.");
                    continue;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            } else {
                // there's a file
                // test the file,
                // if wrong; ask for settings,
                // else; exit loading loop.
                if (testConnection()) {
                    showDialog("wrong settings!!");
                } else {
                    // waarden kloppen uit laad modes gaan!
                    loading = false;
                }
            }
        }
    }*/



    /*
    private boolean testConnection() {
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

    }*/

    public void testConnection() {

    }

    public static List<String> loadConfig() throws IOException {

        // does dir exists? no.., create dir.
        if (!Files.exists(TDIR)) {
            Files.createDirectory(TDIR);
        }

        return Files.readAllLines(TFILE);

    }

    public static void showDialog(String message) {

        showing = true;
        //ConnectionDialog connectionDialog = new ConnectionDialog(Application.screenDimension());
        connectionDialog = new ConnectionDialog(Application.screenDimension());
        connectionDialog.setMessage(message);
        config = null;

        // save button listener.
        connectionDialog.addSaveActionListener(new SaveListener());

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
    }

    public static List<String> getConfig() {
        return config;
    }

    /*
    public static boolean isDirectoryPresent() {
        return Files.exists(TDIR);
    }

    public static void createSyncDir() throws IOException {
        Files.createDirectory(TDIR);
    }

    public static void createIndexFile() throws IOException {
        System.out.println(TFILE.toString());
        Files.createFile(TFILE);
    }

    public static void writeConnectionConfig(List<String> list) throws IOException {
        System.out.println(TFILE.toString());
        Files.write(TFILE, list, Charset.forName("UTF-8"));
    }

    public static List<String> readConnectionConfig() throws IOException {
        return Files.readAllLines(TFILE);
    }*/

    private static class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String host = HOST + " " + connectionDialog.getConfigConnectionView().getHostField();
            String port = PORT + " " + connectionDialog.getConfigConnectionView().getPortField();
            List<String> list = Arrays.asList(host, port);
            try {
                Files.write(TFILE, list, Charset.forName("UTF-8"));
                //ConfigLoader.writeConnectionConfig(list);
            } catch (AccessDeniedException ade) {
                // TODO. als file niet geschreven kan worden? ....
                //
                loading = false;
                System.out.println("Fock!");
                ade.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            // closes the jdialog.
            connectionDialog.dispose();
            synchronized (lock) {
                showing = false;
                lock.notify();
            }
        }
    }


}
