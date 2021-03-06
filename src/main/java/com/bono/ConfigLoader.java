package com.bono;


import com.bono.api.Endpoint;
import com.bono.view.ConnectionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
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

    public static final Path FILE = Paths.get("config.file");


    private static ConnectionDialog connectionDialog;

    public static final String HOST = "HOST";
    public static final String PORT = "PORT";

    private static String path = System.getProperty("user.home") + File.separator + ".zero";
    private static String file = path + File.separator + "config.cfg";
    private static File dirPath = new File(path);
    private static File filePath = new File(file);

    private static Endpoint endpoint = null;

    private static String version;

    private static List<String> config = null;

    private static Object lock = new Object();

    private static boolean loading = true;

    private static boolean showing;

    public ConfigLoader() {
        super();
    }

    public static Properties loadconfig() throws IOException {
        Properties configProperties;

        while (true) {

                if (dirPath.exists()) {
                    if (filePath.exists()) {
                        // everything is there now load it
                        config = loadConfig();
                    } else {
                        // no file display config window
                        ConfigLoader.showDialog("Please give HOST and PORT");
                        waitForInput();
                        continue;
                    }
                } else {
                    // create dir
                    if (dirPath.mkdir()) {
                        // display config
                        ConfigLoader.showDialog("Please give HOST and PORT");
                        waitForInput();
                        continue;
                    } else{
                        throw new IOException();
                    }
                }

            configProperties = new Properties();
            for (String s : config) {

                String[] param = s.split(" ");

                if (param.length > 1) configProperties.setProperty(param[0], param[1]);
            }
            if (configProperties.containsKey(ConfigLoader.HOST) && configProperties.containsKey(ConfigLoader.PORT)) {

                try {
                    version = testConnection(configProperties.getProperty(ConfigLoader.HOST),
                            Integer.parseInt(configProperties.getProperty(ConfigLoader.PORT)));
                    System.out.println(version);
                } catch (SocketTimeoutException ste) {
                    ConfigLoader.showDialog("Time out, wrong settings.");
                    waitForInput();
                    continue;
                } catch (NoRouteToHostException nrh) {
                    ConfigLoader.showDialog("no route, wrong settings.");
                    waitForInput();
                    continue;
                }catch (ConnectException ce) {
                    ConfigLoader.showDialog("refused, wrong settings.");
                    waitForInput();
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ConfigLoader.showDialog("Please give HOST and PORT");
                waitForInput();
                continue;
            }
            break;
        }
        return configProperties;
    }



    private static String testConnection(String host, int port) throws IOException {
        Endpoint endpoint = new Endpoint(host, port);

        String version = endpoint.getVersion();

        return version;
    }


    public static List<String> loadConfig() throws IOException {

        return Files.readAllLines(filePath.toPath());

    }

    public static void showDialog(String message) {

        showing = true;

        connectionDialog = new ConnectionDialog(ApplicationMain.screenDimension());
        connectionDialog.setMessage(message);
        config = null;

        // save button listener.
        connectionDialog.addSaveActionListener(new SaveListener());

        connectionDialog.showDialog();

    }

    private static void waitForInput() {

        if (!SwingUtilities.isEventDispatchThread()) {
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
    }


    private static class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String host = HOST + " " + connectionDialog.getHost();
            String port = PORT + " " + connectionDialog.getPort();
            List<String> list = Arrays.asList(host, port);
            try {
                Files.write(filePath.toPath(), list, Charset.forName("UTF-8"));
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
