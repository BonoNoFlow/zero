package com.bono.playlist;

import com.bono.Idle;
import com.bono.Utils;
import com.bono.api.Song;
import com.bono.api.*;
import com.bono.soundcloud.AdditionalTrackInfo;
import com.bono.soundcloud.SoundcloudController;
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
 *
 * PlaylistController extends the mouseadapter and
 * implements the changelistener.
 *
 * With the changelistener it listens to the playlist
 * for updates.
 *
 * The mouseadpater displays a popup menu supporting
 * several commands.
 *
 */
public class PlaylistController extends MouseAdapter implements ChangeListener {

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    private Playlist playlist = new Playlist();

    private DBExecutor dbExecutor;

    private PlaylistView playlistView;


    public PlaylistController() {}


    public void init() {
        playlistView.addDropTargetListener(new DroppedListener());
        playlistView.addMouseListener(this);
        //playlistView.setModel(getModel());
        playlist.addListener(this);
        playlist.addSongListener(new AdditionalTrackInfo(SoundcloudController.CLIENTID));
        initPlaylist();
        playlistView.setModel(getModel());
    }

    public DefaultListModel<Song> getModel() {
        return songs;
    }

    /*

    ChangeListener

    Listen to the playlist. If the playlist is re-written
    than the JList model is updated in the event dispatch
    thread.

     */
    @Override
    public void stateChanged(ChangeEvent e) {
        //Playlist playlist = (Playlist) e.getSource();

        SwingUtilities.invokeLater(() -> {
            songs.clear();
            Iterator<Song> i = playlist.iterator();
            while (i.hasNext()) {
                songs.addElement(i.next());

                Utils.Log.print(songs.lastElement().getFile());

            }
            //playlistView.getPlaylist().setModel(songs);
            //playlistView.getPlaylist().repaint();
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
                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("play", new PlayListener());
                popup.addMenuItem("remove", new RemoveListener());
                popup.show(playlistView.getPlaylist(), e.getX(), e.getY());
            }
        }
    }


    // listener for play menu item.
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


    // listener for remove menu item.
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


    /*

    DropTargetListener

    The drop target accepts http / https urls, or files
    from the directory view.
     */
    private class DroppedListener extends DropTargetAdapter {

        @Override
        public void drop(DropTargetDropEvent dtde) {

            DataFlavor[] flavors = dtde.getCurrentDataFlavors();

            for (int i = 0; i < flavors.length; i++) {
                System.out.println(flavors[i].getMimeType());
            }
            /*
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
            }*/
        }
    }

    // initiate the playlist model.
    private void initPlaylist() {
        // init playlist.
        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand(PlaylistProperties.PLAYLISTINFO));
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
