package com.bono.controls;

import com.bono.Application;
import com.bono.ConfigLoader;
import com.bono.ConfigPresenter;
import com.bono.view.ConnectionDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bono on 8/18/16.
 */
public class MenuBarController {

    private Application app;

    public MenuBarController(Application app) {
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
