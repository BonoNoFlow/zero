package com.bono.controls;


import com.bono.api.*;
import com.bono.view.MPDPopup;
import com.bono.view.PlaylistView;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;


/**
 * Created by hendriknieuwenhuis on 26/04/16.
 */
public class CurrentPlaylist extends MouseAdapter implements ChangeListener {

    private PlaylistView playlistView;

    private PlaylistControl playlistControl;

    private Playlist playlist;

    private Playback playback;

    private DefaultListModel<Song> songs;

    public CurrentPlaylist(PlaylistControl playlistControl, Playback playback) {
        super();
        playlist = new Playlist();
        this.playlistControl = playlistControl;
        this.playback = playback;
    }

    public void initPlaylist() {
        String value = "";

        try {
            value = playlistControl.playlistinfo(null);
            playlist.clear();
            playlist.populate(value);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @Override
    public void stateChanged(ChangeEvent e) {
        String line = (String) e.getSource();
        if (line.equals("playlist")) {
            initPlaylist();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("Clicked");

            if (!playlistView.getPlaylist().getSelectionModel().isSelectionEmpty()) {
                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("play", new PlayListener());
                popup.addMenuItem("remove", new RemoveListener());
                popup.show(playlistView.getPlaylist(), e.getX(), e.getY());
            }
        }
    }

    public PlaylistView getPlaylistView() {
        return playlistView;
    }

    public void setPlaylistView(PlaylistView playlistView) {
        this.playlistView = playlistView;
    }

    private class PlayListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = playlistView.getPlaylist().getSelectionModel();
            int track = model.getAnchorSelectionIndex();
            Song song = playlist.getSong(track);
            try {
                playback.getPlayerControl().playid(song.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class RemoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = playlistView.getPlaylist().getSelectionModel();
            int track = model.getAnchorSelectionIndex();
            Song song = playlist.getSong(track);

            try {
                playlistControl.deleteId(song.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
