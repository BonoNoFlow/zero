package com.bono;


import com.bono.view.ConnectionDialog;

import javax.swing.*;
;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */

/*
TODO Veranderen in ConnectionOptionsPresenter.
 */
public class ConfigPresenter {

    public final static String HOST = "host";
    public final static String PORT = "port";

    public static final String SAVE = "save";

    private Properties properties;

    private ApplicationMain app;
    /*
    Used for presenting the config properties as a dialog.
     */
    private ConnectionDialog connectionDialog;



    private boolean showing = false;

    /*
    public ConfigPresenter(Properties properties) {
        this.properties = properties;
    }*/

    public ConfigPresenter(ApplicationMain app, String message) {
        this.app = app;
        //if (!SwingUtilities.isEventDispatchThread()) {
        //    SwingUtilities.invokeLater(() -> {
        //        connectionDialog = new ConnectionDialog(ApplicationMain.screenDimension());
        //    });
        //} else {
            connectionDialog = new ConnectionDialog(ApplicationMain.screenDimension());
        //}

        showDialog(message);
    }

    private void showDialog(String message) {
        if (message != null) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> {
                    connectionDialog.setMessage(message);
                });
            } else {
                connectionDialog.setMessage(message);
            }
        }
    }

    public void show() {
        connectionDialog.showDialog();
    }

    public ActionListener saveListener() {
        return event -> {
            String host = ConfigLoader.HOST + " " + connectionDialog.getHost();
            String port = ConfigLoader.PORT + " " + connectionDialog.getPort();
            List<String> list = Arrays.asList(host, port);
            try {
                Files.write(ConfigLoader.FILE, list, Charset.forName("UTF-8"));
                //ConfigLoader.writeConnectionConfig(list);
            } catch (AccessDeniedException ade) {
                // TODO. als file niet geschreven kan worden? ....
                //

                ade.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            // closes the jdialog.
            connectionDialog.dispose();
            //app.loadProperties();
            new Thread(() -> {
                //app.loadProperties();
            }).start();
        };
    }

    public void addSaveListener(ActionListener l) {
        connectionDialog.addSaveActionListener(l);
    }




}
