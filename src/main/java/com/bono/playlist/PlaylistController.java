package com.bono.playlist;

import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.io.IOException;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class PlaylistController extends MouseAdapter {

    private PlaylistView playlistView;
    private DBExecutor dbExecutor;
    private Playlist playlist = new Playlist();

    public PlaylistController(DBExecutor dbExecutor, PlaylistView playlistView) {
        this.dbExecutor = dbExecutor;
        this.playlistView = playlistView;
        init();
    }

    private void init() {
        String entry = "";
        try {
            entry = dbExecutor.execute(new MPDCommand(PlaylistProperties.PLAYLISTINFO));
        } catch (Exception e) {
            e.printStackTrace();
        }
        playlist.populate(entry);
        playlistView.setModel(playlist.getPlaylistModel());
    }

    public void update() {
        init();
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
