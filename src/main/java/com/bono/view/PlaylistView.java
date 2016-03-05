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
    private JScrollPane scrollPane;
    private DropTarget dropTarget;

    public PlaylistView() {
        super();
        build();
    }

    private void build() {
        dropTarget = new DropTarget();
        playlist = new JList();
        dropTarget.setComponent(playlist);
        playlist.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        playlist.setCellRenderer(new PlaylistCellRenderer());
        //scrollPane = new JScrollPane();
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //setLayout(new BorderLayout());
        getViewport().add(playlist, BorderLayout.CENTER);
        //add(scrollPane);
    }

    public void addMouseListener(MouseListener mouseListener) {
        playlist.addMouseListener(mouseListener);
    }

    public void setModel(ListModel model) {
        playlist.setModel(model);
        //playlist.update(playlist.getGraphics());
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
