package com.bono.controls;

import com.bono.api.*;
import com.bono.view.PlaylistView;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.MouseAdapter;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 26/04/16.
 */
public class CurrentPlaylist {

    private PlaylistView playlistView;

    private PlaylistControl playlistControl;

    private Playlist playlist;

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    public CurrentPlaylist(PlaylistControl playlistControl) {
        this.playlistControl = playlistControl;
    }

    // TODO dit kan zonder pPlaylist class!!!!
    public void initPlaylist() {
        playlist = new Playlist();
        String value = "";
        try {
            playlist.populate(playlistControl.playlistinfo(null));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        songs.clear();
        for (Song s : playlist.playlist()) {
            songs.addElement(s);
        }

        if (playlistView != null) {
            SwingUtilities.invokeLater(() -> {
                playlistView.getPlaylist().setModel(songs);
            });
        }
     }

    public PlaylistView getPlaylistView() {
        return playlistView;
    }

    public void setPlaylistView(PlaylistView playlistView) {
        this.playlistView = playlistView;
    }
}
