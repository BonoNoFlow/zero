package com.bono.playlist;

import com.bono.Utils;
import com.bono.api.*;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.view.PlaylistView;
import com.bono.view.SongCellRenderer;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class  PlaylistPresenter extends MouseAdapter {

    private PlaylistView playlistView;

    private Playlist playlist;

    private DefaultListModel<Song> songs;

    private PlaylistTableModel playlistTableModel;

    private ClientExecutor clientExecutor;

    // listeners of this class.
    private PlaylistPresenter.DroppedListener droppedListener;
    private PlaylistPresenter.IdleListener idleListener;

    public PlaylistPresenter(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
        playlist = new Playlist();
        playlistTableModel = new PlaylistTableModel(playlist);
    }

    public PlaylistPresenter(ClientExecutor clientExecutor, Playlist playlist) {
        this.clientExecutor = clientExecutor;
        this.playlist = playlist;
        playlistTableModel = new PlaylistTableModel(this.playlist);
    }

    /*
    Adds the view to the presenter.
    Also adds a droptargetadapter to the view.
     */
    public void addView(PlaylistView playlistView) {
        this.playlistView = playlistView;
        this.playlistView.setModel(playlistTableModel);
        this.playlistView.getColumn(0).setCellRenderer(new SongCellRenderer());
        this.playlistView.getColumn(1).setCellRenderer(new SongCellRenderer());
        this.playlistView.addDropTargetListener(getDroppedListener());
    }

    public void addPlaylistListener(ChangeListener changeListener) {
        playlist.addListener(changeListener);
    }

    public void initPlaylist() {
        Collection<String> response = new ArrayList<>();

        try {
            response = clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTINFO));
            playlist.clear();
            playlist.populate(response);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        playlistTableModel.fireTableDataChanged();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        showPopup(e);
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            playlistView.getSelectionModel().setValueIsAdjusting(false);
            if (!playlistView.getSelectionModel().isSelectionEmpty()) {
                PlaylistPopup p = new PlaylistPopup(clientExecutor, playlistView, playlistTableModel);
                p.show(e.getX(), e.getY());
            }
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }



    public DropTargetListener getDroppedListener() {
        if (droppedListener == null) {
            droppedListener = new PlaylistPresenter.DroppedListener();
        }
        return droppedListener;
    }

    /*

        DropTargetListener

        The drop target accepts http / https urls, or files
        from the database view.
         */
    private class DroppedListener extends DropTargetAdapter {

        @Override
        public void drop(DropTargetDropEvent dtde) {

            String d = "";
            try {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                d = (String) dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(d);


            if (d.startsWith("http") || d.startsWith("https")) {
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlaylist.LOAD, Utils.loadUrl(d)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {

                //Utils.Log.print(d);
            }
        }
    }

    public ChangeListener getIdleListener() {
        if (idleListener == null) {
            idleListener = new PlaylistPresenter.IdleListener();
        }
        return idleListener;
    }

    private class IdleListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            String event = (String) eventObject.getSource();
            if (event.equals("playlist")) {
                initPlaylist();
            }
        }
    }
}
