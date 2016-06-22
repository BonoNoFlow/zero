package com.bono;

import com.bono.api.Config;
import com.bono.view.ConfigConnectionView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 18/06/16.
 */
public class ConfigPresenter {

    public final static String HOST = "host";
    public final static String PORT = "port";

    public static final String SAVE = "save";

    private Config config;

    private ConfigConnectionView configConnectionView;

    private SaveConfig saveConfig = null;

    public ConfigPresenter(Config config) {
        this.config = config;
    }

    public ConfigPresenter(Config config, ConfigConnectionView configConnectionView) {
        this(config);
        this.configConnectionView = configConnectionView;
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
    Deze class / actie moet in Dialog of verder weg.


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
