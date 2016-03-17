package com.bono;

import com.bono.api.*;
import com.bono.view.MPDPopup;
import com.bono.view.PlaylistView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 15/03/16.
 */
public class PlaylistController extends MouseAdapter implements ChangeListener {

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    private Playlist playlist = new Playlist();

    private JList list;

    private ListSelectionModel selectionModel;

    private DBExecutor dbExecutor;

    private PlaylistView playlistView;

    public PlaylistController() {}

    public PlaylistController(DBExecutor dbExecutor, PlaylistView playlistView) {
        this.dbExecutor = dbExecutor;
        this.playlistView = playlistView;
        playlistView.addDropTargetListener(new DroppedListener());
        playlistView.addMouseListener(this);
        playlistView.setModel(getModel());
        playlist.addListener(this);
        initPlaylist();
    }

    public PlaylistController(DBExecutor dbExecutor, JList list, Playlist playlist) {
        this.dbExecutor =dbExecutor;
        this.list = list;
        this.list.setModel(getModel());
        this.selectionModel = list.getSelectionModel();
        this.playlist = playlist;
        this.playlist.addListener(this);
    }

    public void init() {
        playlistView.addDropTargetListener(new DroppedListener());
        playlistView.addMouseListener(this);
        playlistView.setModel(getModel());
        playlist.addListener(this);
        initPlaylist();
    }

    public DefaultListModel<Song> getModel() {
        return songs;
    }


    /*

    ChangeListener

    Listen to the playlist. If the playlist is re-written
    than the JList is updated.

     */
    @Override
    public void stateChanged(ChangeEvent e) {
        Playlist playlist = (Playlist) e.getSource();

        SwingUtilities.invokeLater(() -> {
            songs.clear();
            Iterator<Song> i = playlist.iterator();
            while (i.hasNext()) {
                songs.addElement(i.next());

                Utils.Log.print(songs.lastElement().getFile());

            }
            //list.revalidate();
        });

    }

    /*

    MouseAdapter

    Listen to the mouse events in the JList. When
    selection is true a popup window is shown
    whith the options:
    'play'
    'remove'
    The inner classes PlayListener or RemoveListener
    is called when an option is pushed.

     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (e.getButton() == MouseEvent.BUTTON3) {

            if (!playlistView.getListSelectionModel().isSelectionEmpty()) {
                //Song song = songs.get(list.getSelectionModel().getAnchorSelectionIndex());
                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("play", new PlayListener());
                popup.addMenuItem("remove", new RemoveListener());
                popup.show(playlistView.getPlaylist(), e.getX(), e.getY());
            }
        }
    } // end mouseadapter.

    private class PlayListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int track = playlistView.getListSelectionModel().getAnchorSelectionIndex();
            Song song = playlist.getSong(track);
            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAYID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Utils.Log.print(getClass().toString() + " " + reply);

        }
    }

    private class RemoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int track = playlistView.getListSelectionModel().getAnchorSelectionIndex();
            Song song = playlist.getSong(track);
            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand(PlaylistProperties.DELETE_ID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Utils.Log.print(getClass().toString() + " " + reply);

        }
    }

    //public DropTargetListener dropTargetListener() {
    //    return new DropedListener();
    //}

    /*

    DropTargetListener

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

            String reply = "";
            if (d.startsWith("http") || d.startsWith("https")) {
                try {
                    reply = dbExecutor.execute(new MPDCommand("load", Utils.loadUrl(d)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {

                Utils.Log.print(d);
            }
        }
    }

    private void initPlaylist() {
        // init playlist.
        //System.out.println("going to initiate playlist");
        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand("playlistinfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        playlist.populate(reply);
    }

    public ChangeListener getIdlePlaylistListener() {
        return new IdlePlaylistListener();
    }

    // listener updates playlist model.
    private class IdlePlaylistListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            String message = (String) e.getSource();
            if (message.equals(Idle.PLAYLIST)) {
                initPlaylist();
                System.out.println("Playlist initiated!");

            }
        }

    }

    public DBExecutor getDbExecutor() {
        return dbExecutor;
    }

    public void setDbExecutor(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public PlaylistView getPlaylistView() {
        return playlistView;
    }

    public void setPlaylistView(PlaylistView playlistView) {
        this.playlistView = playlistView;
    }
}
