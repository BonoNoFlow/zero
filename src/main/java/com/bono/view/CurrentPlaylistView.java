package com.bono.view;

import com.bono.view.renderers.PlaylistRenderer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;

/**
 * Created by bono on 8/17/16.
 */
public class CurrentPlaylistView extends JScrollPane implements PlaylistView {

    private JTable playlistTable;

    private JList playlist;

    private DropTarget dropTarget;

    public CurrentPlaylistView() {
        super();
        build();
    }

    private void build() {
        playlistTable = new JTable();
        //playlistTable = new PlaylistTable();
        playlist = new JList();
        playlist.setCellRenderer(new PlaylistRenderer());
        playlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //playlistTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //playlistTable.setRowHeight(40);
        dropTarget = new DropTarget();
        dropTarget.setComponent(this);
        //getViewport().add(playlistTable);
        getViewport().add(playlist);
    }

    @Override
    public void setModel(ListModel model) {
        playlist.setModel(model);
    }

    @Override
    public void setModel(TableModel model) {
        playlistTable.setModel(model);
    }

    @Override
    public TableColumn getColumn(int index) {
        return playlistTable.getColumnModel().getColumn(index);
    }

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
        //addDropTargetListener(l);
    }

    @Override
    public void addMouseListener(MouseListener l) {
        playlist.addMouseListener(l);
        //playlistTable.addMouseListener(l);
    }
}
