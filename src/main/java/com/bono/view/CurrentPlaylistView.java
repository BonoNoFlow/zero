package com.bono.view;

import com.bono.view.renderers.PlayingRenderer;
import com.bono.view.renderers.PlaylistRenderer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.util.TooManyListenersException;

/**
 * Created by bono on 8/17/16.
 */
public class CurrentPlaylistView extends JScrollPane implements PlaylistView {

    private JTable playlistTable;

    private JList playlist;

    private DropTarget dropTarget;

    private PlaylistRenderer playlistRenderer = new PlaylistRenderer();

    public CurrentPlaylistView() {
        super();
        build();
    }

    private void build() {
        playlistTable = new JTable();
        playlist = new JList();
        playlist.setCellRenderer(playlistRenderer);
        playlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dropTarget = new DropTarget();
        dropTarget.setComponent(this);
        getViewport().add(playlist);
    }

    @Override
    public void setModel(ListModel model) {
        playlist.setModel(model);
    }

    /*
    @Override
    public void setModel(TableModel model) {
        playlistTable.setModel(model);
    }

    @Override
    public TableColumn getColumn(int index) {
        return playlistTable.getColumnModel().getColumn(index);
    }*/

    @Override
    public ListSelectionModel getSelectionModel() {
        return playlist.getSelectionModel();
        //return playlistTable.getSelectionModel();
    }

    @Override
    public int[] getSelectedRows() {
        return playlist.getSelectedIndices();
        // playlistTable.getSelectedRows();
    }

    @Override
    public boolean isRowSelected(int row) {
        return playlist.isSelectedIndex(row);
        //return playlistTable.isRowSelected(row);
    }

    @Override
    public Component getComponent() {
        return playlist;
        //return playlistTable;
    }

    @Override
    public void addDropTargetListener(DropTargetListener l) {
        //this.addDropTargetListener(l);
        try {
            dropTarget.addDropTargetListener(l);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMouseListener(MouseListener l) {
        playlist.addMouseListener(l);
        //playlistTable.addMouseListener(l);
    }

    @Override
    public PlayingRenderer getPlayingRenderer() {
        return playlistRenderer;
    }

    @Override
    public void redraw() {
        playlist.repaint();
        playlist.revalidate();
    }
}
