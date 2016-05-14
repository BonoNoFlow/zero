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
public class CurrentPlaylist extends MouseAdapter implements ListDataListener {

    private PlaylistView playlistView;

    private PlaylistControl playlistControl;

    private Playlist playlist;

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    public CurrentPlaylist(PlaylistControl playlistControl) {
        this.playlistControl = playlistControl;
        songs.addListDataListener(this);
        //initPlaylist();
    }

    /*
    public CurrentPlaylist(DBExecutor dbExecutor, boolean initPlaylist) {
        this(dbExecutor);
        if (initPlaylist) initPlaylist();
    }

    public CurrentPlaylist(DBExecutor dbExecutor, PlaylistView playlistView, boolean initPlaylist) {
        this(dbExecutor);
        this.playlistView = playlistView;
        if (initPlaylist) initPlaylist();
    }*/


    // TODO dit kan zonder pPlaylist class!!!!
    public void initPlaylist() {
        playlist = new Playlist();
        String value = "";
        songs.clear();
        try {
            playlist.populate(playlistControl.playlistinfo(null));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<Song> i = playlist.iterator();
        while (i.hasNext()) {
            songs.addElement(i.next());
        }
        System.out.println("donr!");
        if (playlistView != null) {
            SwingUtilities.invokeLater(() -> {
                playlistView.getPlaylist().setModel(songs);
            });
        }

     }

    @Override
    public void intervalAdded(ListDataEvent e) {
        System.out.println("added");
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        System.out.println("removed");
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        System.out.println("Data list changed");
        DefaultListModel<Song> list = (DefaultListModel<Song>) e.getSource();

        if (playlistView != null) {
            SwingUtilities.invokeLater(() -> {
                playlistView.getPlaylist().setModel(list);
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
