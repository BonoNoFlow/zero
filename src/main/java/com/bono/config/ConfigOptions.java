package com.bono.config;

import com.bono.api.Config;
import com.bono.view.ConfigOptionsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by hendriknieuwenhuis on 25/03/16.
 */
public class ConfigOptions implements ActionListener {

    private ConfigOptionsView view;

    private Config config;

    private Thread thread;

    private boolean showing = false;

    public ConfigOptions(Config config) throws InvocationTargetException, InterruptedException {
        this.config = config;

        view = new ConfigOptionsView();
        view.addListener(this);
        view.show();
        showing = true;
        thread = Thread.currentThread();
        showing();



    }

    public Thread currentThread() {
        return thread;
    }

    private void showing() {
        synchronized (thread) {
            while (showing) {
                try {

                    thread.wait();
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
        synchronized (thread) {
            thread.notify();
        }
        view.dispose();
    }
}
