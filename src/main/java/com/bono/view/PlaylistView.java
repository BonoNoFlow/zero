package com.bono.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 28/02/16.
 */
public class PlaylistView extends JPanel {

    private JList playlist;
    private JScrollPane scrollPane;

    public PlaylistView() {
        super();
        build();
    }

    private void build() {
        playlist = new JList();
        playlist.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(playlist);
        add(scrollPane);
    }

    public void addMouseListener(MouseListener mouseListener) {
        playlist.addMouseListener(mouseListener);
    }

    public void setModel(ListModel model) {
        playlist.setModel(model);
    }
}
