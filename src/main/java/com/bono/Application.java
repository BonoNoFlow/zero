package com.bono;

import com.bono.models.Config;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class Application {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    private Config config;

    private  DBExecutor dbExecutor;
    public Application() {
        init();
        build();
    }

    private void init() {
        config = new Config();
        // dit moet vervangen worden met loadparams in try catch!
        // TODO verschillende methods!!
        // TODO params moeten ook getsets worden.
        try {
            config.loadParams();
        } catch (Exception e) {
            ConfigOptions configOptions = new ConfigOptions(config);
            //config.loadParams();
        } finally {
            config.setHost("192.168.2.4");
            config.setPort(6600);
            dbExecutor = new DBExecutor(config);
        }


    }

    private void build() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView();
            soundcloudController = new SoundcloudController(dbExecutor, applicationView.getSoundcloudPanel());

            applicationView.view();
        });
    }

    public static void main(String[] args) {
        new Application();
    }
}
