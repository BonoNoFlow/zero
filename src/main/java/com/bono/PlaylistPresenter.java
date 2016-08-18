package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.controls.*;
import com.bono.view.CurrentPlaylist;
import com.bono.view.MPDPopup;
import com.bono.view.PlaylistView;
import com.bono.view.SongCellRenderer;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class  PlaylistPresenter extends MouseAdapter implements ChangeListener {

    private PlaylistView playlistView;

    private CurrentPlaylist currentPlaylist;

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
        playlist.addListener(this);

    }

    /*
    Adds the view to the presenter.
    Also adds a droptargetadapter to the view.
     */
    public void addView(PlaylistView view) {
        playlistView = view;
        playlistView.addDropTargetListener(getDroppedListener());
    }

    public void addView(CurrentPlaylist currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
        this.currentPlaylist.setModel(playlistTableModel);
        this.currentPlaylist.getColumn(0).setCellRenderer(new SongCellRenderer());
        this.currentPlaylist.getColumn(1).setCellRenderer(new SongCellRenderer());
        this.currentPlaylist.addDropTargetListener(getDroppedListener());
    }

    public void addSongListener(ChangeListener changeListener) {
        playlist.addSongListener(changeListener);
    }

    public void initPlaylist() {
        List<String> response = new ArrayList<>();

        try {
            response = clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTINFO));
            playlist.clear();
            playlist.populate(response);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //playlistTableModel.fireTableDataChanged();
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

            if (!currentPlaylist.getSelectionModel().isSelectionEmpty()) {
                PlaylistPopup p = new PlaylistPopup(clientExecutor, currentPlaylist , playlistTableModel);
                p.show(e.getX(), e.getY());
            }
        }
    }

    // listens to changes in the playlist.
    @Override
    public void stateChanged(EventObject eventObject) {

        playlistTableModel.fireTableDataChanged();
    }

    public Song song(String id) {
        Song song = null;
        try {
            song =  new Song(clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTID, id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return song;
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
