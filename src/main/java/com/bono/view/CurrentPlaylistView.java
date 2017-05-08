package com.bono.view;

import com.bono.laf.BonoScrollBarUI;
import com.bono.view.renderers.PlayingRenderer;
import com.bono.view.renderers.PlaylistRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.util.TooManyListenersException;

/**
 * Created by bono on 8/17/16.
 */
public class CurrentPlaylistView extends JScrollPane implements PlaylistView {

    private JList playlist;

    private DropTarget dropTarget;

    private PlaylistRenderer playlistRenderer = new PlaylistRenderer();

    public CurrentPlaylistView() {
        super();
        build();
    }

    private void build() {
        setBorder(null);
        getHorizontalScrollBar().setUI(new BonoScrollBarUI());
        getVerticalScrollBar().setUI(new BonoScrollBarUI());
        playlist = new JList();
        playlist.setBorder(null);
        playlist.setCellRenderer(playlistRenderer);
        playlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        playlist.setDragEnabled(true);
        playlist.setDropMode(DropMode.INSERT);
        dropTarget = new DropTarget();
        dropTarget.setComponent(this);
        getViewport().add(playlist);
    }

    @Override
    public void setModel(ListModel model) {
        playlist.setModel(model);
    }

    @Override
    public ListSelectionModel getSelectionModel() {
        return playlist.getSelectionModel();
    }

    @Override
    public int[] getSelectedRows() {
        return playlist.getSelectedIndices();
    }

    @Override
    public boolean isRowSelected(int row) {
        return playlist.isSelectedIndex(row);
    }

    @Override
    public Component getComponent() {
        return playlist;
    }

    @Override
    public void addDropTargetListener(DropTargetListener l) {
        try {
            dropTarget.addDropTargetListener(l);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTransferHandler(TransferHandler t) {
        playlist.setTransferHandler(t);
    }

    @Override
    public void addMouseListener(MouseListener l) {
        playlist.addMouseListener(l);
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
