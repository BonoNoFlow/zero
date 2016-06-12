package com.bono.directory;

import com.bono.Utils;
import com.bono.api.*;
import com.bono.controls.Player;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.util.EventObject;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class PPresentor implements ChangeListener {

    private PlaylistView playlistView;

    private Playlist playlist;

    private Player player;

    private DefaultListModel<Song> songs;

    private DBExecutor dbExecutor;

    // listeners of this class.
    private PPresentor.DroppedListener droppedListener;
    private PPresentor.IdleListener idleListener;

    public PPresentor(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        playlist = new Playlist();
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

    public void initPlaylist() {
        String value = "";

        try {
            value = dbExecutor.execute(new DefaultCommand(Playlist.PLAYLISTINFO));
            playlist.clear();
            playlist.populate(value);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stateChanged(EventObject eventObject) {
        Playlist playlist = (Playlist) eventObject.getSource();
        songs = new DefaultListModel<>();
        Iterator<Song> i = playlist.iterator();
        while (i.hasNext()) {
            songs.addElement(i.next());
        }
        if (playlistView != null) {
            SwingUtilities.invokeLater(() -> {
                playlistView.getPlaylist().setModel(songs);
            });
        }
    }

    public Song song(String id) {
        Song song = null;
        try {
            song =  new Song(dbExecutor.execute(new DefaultCommand(Playlist.PLAYLISTID, id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return song;
    }

    public DropTargetListener getDroppedListener() {
        if (droppedListener == null) {
            droppedListener = new PPresentor.DroppedListener();
        }
        return droppedListener;
    }

    /*

        DropTargetListener

        The drop target accepts http / https urls, or files
        from the directory view.
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

            String reply = "";
            if (d.startsWith("http") || d.startsWith("https")) {
                try {
                    reply = dbExecutor.execute(new DefaultCommand(Playlist.LOAD, Utils.loadUrl(d)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {

                Utils.Log.print(d);
            }
        }
    }

    public ChangeListener getIdleListener() {
        if (idleListener == null) {
            idleListener = new PPresentor.IdleListener();
        }
        return idleListener;
    }

    private class IdleListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            String event = (String) eventObject.getSource();
            if (event.equals("playlist")) {
                //initPlaylist();
            }
        }
    }
}
