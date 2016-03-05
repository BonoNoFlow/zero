package com.bono.playlist;

import com.bono.api.Playlist;
import com.bono.api.Song;

import javax.swing.*;
import java.util.Iterator;

/**
 * Created by bono on 3/4/16.
 */
public class MPDPlaylist extends Playlist {

    private DefaultListModel<Song> songList;

    public MPDPlaylist() {
        super();
    }

    public DefaultListModel<Song> getModel() {
        songList = new DefaultListModel<>();
        Iterator<Song> i = songs.iterator();
        while (i.hasNext()) {
            songList.addElement(i.next());
        }
        return songList;
    }
}
