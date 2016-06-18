package com.bono;

import com.bono.api.Config;
import com.bono.view.ConfigOptionsView;

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

    private ConfigOptionsView configOptionsView;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ConfigOptionsView getConfigOptionsView() {
        return configOptionsView;
    }

    public void setConfigOptionsView(ConfigOptionsView configOptionsView) {
        this.configOptionsView = configOptionsView;
    }

    private class SaveConfig implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            if (action.equals(ConfigPresenter.SAVE)) {
                config.setProperty(ConfigPresenter.HOST, configOptionsView.getHostField());
                config.setProperty(ConfigPresenter.PORT, configOptionsView.getPortField());
            }
        }
    }
}
