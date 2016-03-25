package com.bono.config;

import com.bono.api.Config;
import com.bono.view.ConfigOptionsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 25/03/16.
 */
public class ConfigOptions implements ActionListener {

    private ConfigOptionsView view;

    private Config config;

    public ConfigOptions(Config config) {
        this.config = config;
        this.view = view;
        SwingUtilities.invokeLater(() -> {
            view = new ConfigOptionsView();
            view.addListener(this);
            view.show();
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        config.setHost(view.getHostField());
        config.setPort(new Integer(view.getPortField()));
        try {
            config.saveParamChanges();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        view.dispose();
    }
}
