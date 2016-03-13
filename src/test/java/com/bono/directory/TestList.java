package com.bono.directory;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Playlist;
import com.bono.api.Song;
import com.bono.config.Config;
import com.bono.playlist.PlaylistController;
import com.bono.view.PlaylistCellRenderer;

import javax.swing.*;
import java.util.Vector;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR;

/**
 * Created by hendriknieuwenhuis on 13/03/16.
 */
public class TestList {

    JList list;

    //Vector<Song> songs;

    Playlist playlist;

    PlaylistController playlistController;

    DBExecutor dbExecutor;

    Config config = new Config("192.168.2.4", 6600);

    public  TestList() {
        dbExecutor = new DBExecutor(config);

        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand("playlistinfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        playlist = new Playlist();
        playlist.populate(reply);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            JScrollPane pane = new JScrollPane();
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            list = new JList(playlist.getSongVector().toArray());
            list.setCellRenderer(new PlaylistCellRenderer());
            pane.getViewport().add(list);
            playlistController = new PlaylistController(dbExecutor,list);
            frame.getContentPane().add(pane);
            frame.pack();
            frame.setVisible(true);
        });
    }



    public static void main(String[] args) {
        new TestList();
    }
}
