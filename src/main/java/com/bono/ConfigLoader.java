package com.bono;

import com.bono.api.ClientExecutor;
import com.bono.api.Config;
import com.bono.api.Endpoint;
import com.bono.view.ConnectionDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */

// Todo file kan niet geschreven worden.
public class ConfigLoader {

    //private static final Path DIR = Paths.get(System.getProperty("user.home") + "/.zero");
    private static final Path TDIR = Paths.get(".zero");
    private static final Path FILE = Paths.get("config.file");
    //private static final Path TFILE = Paths.get(TDIR + "/config.file");

    private static ConnectionDialog connectionDialog;

    public static final String HOST = "HOST";
    public static final String PORT = "PORT";
    public static final String VERSION = "VERSION";

    //private static String[] hosts = {"192.168.2.1", "192.168.2.2", "192.168.2.3", "192.168.2.4"};

    private static Endpoint endpoint = null;

    private static String version;

    private static List<String> config = null;

    private static Object lock = new Object();

    private static boolean loading = true;

    private static boolean showing;

    public ConfigLoader() {
        super();
    }

    public static Properties loadconfig() {
        Properties configProperties;
        int x = 1;
        while (true) {
            System.out.println(x++);
            try {
                config = loadConfig();
            } catch (NoSuchFileException nsf) {
                ConfigLoader.showDialog("No file. Give info.");
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            configProperties = new Properties();
            for (String s : config) {
                String[] param = s.split(" ");
                //properties.setProperty(param[0], param[1]);

                if (param.length > 1) configProperties.setProperty(param[0], param[1]);
            }
            if (configProperties.containsKey(ConfigLoader.HOST) && configProperties.containsKey(ConfigLoader.PORT)) {

                try {
                    version = testConnection(configProperties.getProperty(ConfigLoader.HOST),
                            Integer.parseInt(configProperties.getProperty(ConfigLoader.PORT)), 4000);
                    System.out.println(version);
                } catch (SocketTimeoutException ste) {
                    ConfigLoader.showDialog("Time out, wrong settings.");
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ConfigLoader.showDialog("Please give HOST and PORT");
                continue;
            }
            break;
        }
        return configProperties;
    }



    private static String testConnection(String host, int port, int timeout) throws IOException {
        Endpoint endpoint = new Endpoint(host, port);
        String version = endpoint.getVersion(timeout);
        return version;
    }


    public static List<String> loadConfig() throws IOException {

        // does dir exists? no.., create dir.
        if (!Files.exists(TDIR)) {
            Files.createDirectory(TDIR);
        }

        return Files.readAllLines(FILE);

    }

    private static void showDialog(String message) {

        showing = true;

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


    private static class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String host = HOST + " " + connectionDialog.getConfigConnectionView().getHostField();
            String port = PORT + " " + connectionDialog.getConfigConnectionView().getPortField();
            List<String> list = Arrays.asList(host, port);
            try {
                Files.write(FILE, list, Charset.forName("UTF-8"));
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
