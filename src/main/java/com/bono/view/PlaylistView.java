package com.bono.view;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.util.TooManyListenersException;

/**
 * Created by hendriknieuwenhuis on 28/02/16.
 */
public class PlaylistView extends JScrollPane {

    private JList playlist;

    private DropTarget dropTarget;


    public PlaylistView() {
        super();
        this.playlist = new JList();
        build();
    }


    public PlaylistView(JList list) {
        super();
        this.playlist = list;
        build();
    }


    private void build() {
        dropTarget = new DropTarget();
        dropTarget.setComponent(playlist);
        playlist.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        playlist.setCellRenderer(new PlaylistCellRenderer());
        getViewport().add(playlist, BorderLayout.CENTER);
    }

    public void addMouseListener(MouseListener mouseListener) {
        playlist.addMouseListener(mouseListener);
    }

    public void setModel(ListModel model) {
        playlist.setModel(model);
    }

    public void addDropTargetListener(DropTargetListener listener) {
        try {
            dropTarget.addDropTargetListener(listener);
        } catch (TooManyListenersException t) {
            t.printStackTrace();
        }
    }

    public ListSelectionModel getListSelectionModel() {
        return playlist.getSelectionModel();
    }

    public ListModel getListModel() {
        return playlist.getModel();
    }

    public JList getPlaylist() {
        return playlist;
    }
}
