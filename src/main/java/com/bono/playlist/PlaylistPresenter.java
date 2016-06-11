package com.bono.playlist;

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
 * Created by hendriknieuwenhuis on 06/05/16.
 *
 * This class is changelistener for Playlist.
 *
 *  When the playlist is changed idle triggers
 * the method intiPlaylist() to write the
 * Playlist model.
 *  When the model is written the playlist
 * listener is triggered and sets the view
 * model.
 *
 */
public class PlaylistPresenter implements ChangeListener {

    private PlaylistView playlistView;

    private Playlist playlist;

    private Player player;

    private DefaultListModel<Song> songs;

    private DBExecutor dbExecutor;

    // listeners of this class.
    private DroppedListener droppedListener;
    private IdleListener idleListener;


    public PlaylistPresenter(DBExecutor dbExecutor, Player player) {
        this.player = player;
        this.dbExecutor = dbExecutor;
        playlist = new Playlist();
        playlist.addListener(this);
    }

    private void init() {

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

        /*



        songs = new DefaultListModel<>();
        Iterator<Song> i = playlist.iterator();
        while (i.hasNext()) {
            songs.addElement(i.next());
        }
        if (playlistView != null) {
            SwingUtilities.invokeLater(() -> {
                playlistView.getPlaylist().setModel(songs);
            });
        }*/
    }

    public Song getSong(String songid) {
        Song song = new Song();
        return song;
    }

    @Override
    public void stateChanged(EventObject eventObject) {
        Playlist playlist = (Playlist) eventObject.getSource();
        //playlistView.setModel();
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

    public DropTargetListener getDroppedListener() {
        if (droppedListener == null) {
            droppedListener = new DroppedListener();
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
            idleListener = new IdleListener();
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
