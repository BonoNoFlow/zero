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

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    public CurrentPlaylist(DBExecutor dbExecutor) {
        playlistControl = new PlaylistControl(dbExecutor);
        songs.addListDataListener(this);
    }

    public CurrentPlaylist(DBExecutor dbExecutor, boolean initPlaylist) {
        this(dbExecutor);
        if (initPlaylist) initPlaylist();
    }

    public CurrentPlaylist(DBExecutor dbExecutor, PlaylistView playlistView, boolean initPlaylist) {
        this(dbExecutor);
        this.playlistView = playlistView;
        if (initPlaylist) initPlaylist();
    }

    public void initPlaylist() {
        String value = "";
        songs.clear();
        try {
            value = playlistControl.playlistinfo(null);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Reply reply = new Reply(value);
        Iterator<Song> i = reply.iterator();
        while (i.hasNext()) {
            songs.addElement(i.next());
        }
     }

    @Override
    public void intervalAdded(ListDataEvent e) {

    }

    @Override
    public void intervalRemoved(ListDataEvent e) {

    }

    @Override
    public void contentsChanged(ListDataEvent e) {
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
