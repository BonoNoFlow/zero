package com.bono.view;

import com.bono.PlaylistTable;

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
    //private PlaylistTable playlistTable;

    private DropTarget dropTarget;

    public CurrentPlaylistView() {
        super();
        build();
    }

    private void build() {
        playlistTable = new JTable();
        //playlistTable = new PlaylistTable();
        playlistTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        playlistTable.setRowHeight(40);
        dropTarget = new DropTarget();
        dropTarget.setComponent(this);
        getViewport().add(playlistTable);
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
        return playlistTable.getSelectionModel();
    }

    @Override
    public int[] getSelectedRows() {
        return playlistTable.getSelectedRows();
    }

    @Override
    public boolean isRowSelected(int row) {
        return playlistTable.isRowSelected(row);
    }

    @Override
    public Component getComponent() {
        return playlistTable;
    }

    @Override
    public void addDropTargetListener(DropTargetListener l) {
        //addDropTargetListener(l);
    }

    @Override
    public void addMouseListener(MouseListener l) {
        playlistTable.addMouseListener(l);
    }
}
