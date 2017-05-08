package com.bono.controls;

import com.bono.ApplicationMain;
import com.bono.ConfigPresenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bono on 8/18/16.
 */
public class MenuBarController {

    private ApplicationMain app;

    public MenuBarController(ApplicationMain app) {
        this.app = app;
    }

    private class ConfigMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ConfigPresenter config = new ConfigPresenter(app, null);
            config.addSaveListener(config.saveListener());
            config.show();
        }
    }

    public ActionListener configMenuItemListener() {
        return new ConfigMenuListener();
    }

    private class SavePlaylistMenuItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("savedPlaylist");
        }
    }

    public  ActionListener savePlaylistMenuItemListener() {
        return new SavePlaylistMenuItemListener();
    }
}
