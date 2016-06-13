package com.bono.directory;

import com.bono.PlaylistPresenter;
import com.bono.api.DBExecutor;

import com.bono.view.PlaylistView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 07/06/16.
 */
public class TestPlaylistPresenter {

    JFrame frame;

    public TestPlaylistPresenter() {
        DBExecutor dbExecutor = new DBExecutor("192.168.2.4", 6600);
        PlaylistPresenter playlistPresenter = new PlaylistPresenter(dbExecutor);

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PlaylistView playlistView = new PlaylistView();
            playlistPresenter.addView(playlistView);
            playlistPresenter.initPlaylist();
            frame.getContentPane().add(playlistView);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new TestPlaylistPresenter();
    }
}
