package com.bono;

import com.bono.api.Config;
import com.bono.view.ConfigConnectionView;
import com.bono.view.ConnectionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

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

    private Config config;

    /*
    The Jpanel view of the connection options.
     */
    private ConfigConnectionView configConnectionView;

    /*
    Used for presenting the config options as a dialog.
     */
    private ConnectionDialog connectionDialog;

    private SaveConfig saveConfig = null;

    private boolean showing = false;

    public ConfigPresenter(Config config) {
        this.config = config;
    }

    public ConfigPresenter(Config config, ConfigConnectionView configConnectionView) {
        this(config);
        this.configConnectionView = configConnectionView;
    }

    public void showing() throws InvocationTargetException, InterruptedException{
        synchronized (config) {
            while (showing) {
                try {

                    config.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public  void setConfigConnectionView(ConfigConnectionView configConnectionView) {
        this.configConnectionView = configConnectionView;

    }

    public ActionListener getSaveActionListener() {
        if (saveConfig == null) {
            saveConfig = new SaveConfig();
        }
        return saveConfig;
    }

    /*
    Listener for the save button in the ConnectionDialog.
     */
    private class SaveConfig implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            if (action.equals(ConfigPresenter.SAVE)) {
                config.setProperty(ConfigPresenter.HOST, configConnectionView.getHostField());
                config.setProperty(ConfigPresenter.PORT, configConnectionView.getPortField());

                if (!SwingUtilities.isEventDispatchThread()) {
                    SwingUtilities.invokeLater(() -> {
                        configConnectionView.setHostField("");
                        configConnectionView.setPortField("");

                    });
                } else {
                    configConnectionView.setHostField("");
                    configConnectionView.setPortField("");
                }

                try {
                    config.saveConfig();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        }
    }
}
