package com.bono.playlist;

import com.bono.api.Playlist;
import com.bono.api.Reply;
import com.bono.api.Song;

import javax.swing.*;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 06/05/16.
 */
public class PlaylistModel {

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    public void populate (String entry) {

        Reply reply = new Reply(entry);
        Iterator i = reply.iterator();

        SwingUtilities.invokeLater(() -> {
            songs.clear();
            while (i.hasNext()) {
                songs.addElement(new Song((String)i.next()));
            }
        });
    }

}
