package com.bono;

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

    public Application() {
        init();
        build();
    }

    private void init() {
        executorService = Executors.newFixedThreadPool(3);
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
