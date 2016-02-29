package com.bono.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 28/11/15.
 */
public class PlaybackSettingsPanel extends JPanel {

    private JButton settings;

    public PlaybackSettingsPanel() {
        super();
        build();
    }

    private void build() {
        settings = new JButton("4");
        settings.setActionCommand("settings");

        add(settings);
    }

    public void addMouseListener(MouseListener listener) {
        settings.addMouseListener(listener);
    }
}
