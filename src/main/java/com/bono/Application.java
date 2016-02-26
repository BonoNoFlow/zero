package com.bono;

import com.bono.models.Config;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class Application {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    private ExecutorService executorService;

    private MPDExecutorThread mpdExecutorThread;

    private Config config;

    public Application() {
        init();
        build();
    }

    private void init() {
        config = new Config();
        mpdExecutorThread = new MPDExecutorThread(config);
    }

    private void build() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView();
            soundcloudController = new SoundcloudController(executorService, applicationView.getSoundcloudPanel());

            applicationView.view();
        });
    }

    public static void main(String[] args) {
        new Application();
    }
}
