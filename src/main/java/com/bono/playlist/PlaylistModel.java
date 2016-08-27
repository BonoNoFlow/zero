package com.bono.playlist;

import com.bono.api.Playlist;
import com.bono.api.Song;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * Created by bono on 8/27/16.
 */
public class PlaylistModel extends AbstractListModel<Song> {

    private Playlist playlist;

    public PlaylistModel(Playlist playlist) {
        super();
        this.playlist = playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        fireContentsChanged(playlist, 0, playlist.getSize());
    }

    @Override
    public int getSize() {
        return playlist.getSize();
    }

    @Override
    public Song getElementAt(int index) {
        return playlist.getSong(index);
    }

}
