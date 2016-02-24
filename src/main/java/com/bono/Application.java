package com.bono;

import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class Application {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    public Application() {
        init();
        build();
    }

    private void init() {}

    private void build() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView();
            soundcloudController = new SoundcloudController(applicationView.getSoundcloudPanel());

            applicationView.view();
        });
    }

    public static void main(String[] args) {
        new Application();
    }
}
