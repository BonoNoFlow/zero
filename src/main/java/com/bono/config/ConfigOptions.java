package com.bono.config;

import com.bono.Application;
import com.bono.api.Config;
import com.bono.view.ConfigOptionsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by hendriknieuwenhuis on 25/03/16.
 */
public class ConfigOptions extends WindowAdapter implements ActionListener {

    private ConfigOptionsView view;

    private Config config;

    private boolean showing = false;

    private boolean saved = false;

    public ConfigOptions(Config config) throws InvocationTargetException, InterruptedException {
        this.config = config;

        view = new ConfigOptionsView();
        view.addListener(this);
        view.addWindowListener(this);
        Dimension screen = Application.screenDimension();
        view.placement((screen.width / 2 - (view.getSize().width / 2)), (screen.height / 2 - (view.getSize().height / 2)));
        view.show();
        showing = true;
        showing();
    }

    private void showing() {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        config.setHost(view.getHostField());
        config.setPort(new Integer(view.getPortField()));
        try {
            config.saveParamChanges();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        showing = false;
        saved = true;
        synchronized (config) {
            config.notify();
        }
        view.dispose();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);
        if (!saved) System.exit(0);
    }
}
