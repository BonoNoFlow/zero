package com.bono.playlist;

import com.bono.api.Playlist;
import com.bono.api.Song;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.properties.PlayerProperties;
import com.bono.view.PlaylistView;
import com.bono.view.popup.PlaylistPopup;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class PlaylistController extends MouseAdapter {

    private PlaylistView playlistView;
    private DBExecutor dbExecutor;
    private MPDPlaylist playlist = new MPDPlaylist();

    public PlaylistController(DBExecutor dbExecutor, PlaylistView playlistView) {
        this.dbExecutor = dbExecutor;
        this.playlistView = playlistView;
        this.playlistView.addMouseListener(this);
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
        playlistView.setModel(playlist.getModel());
    }

    public void update() {
        init();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (e.getButton() == MouseEvent.BUTTON3) {

            ListSelectionModel model = ((JList) e.getSource()).getSelectionModel();

            if (!model.isSelectionEmpty()) {

                PlaylistPopup playlistPopup = new PlaylistPopup();

                playlistPopup.addPlayListener(event -> {
                    int track = model.getAnchorSelectionIndex();

                    Song song = playlist.getSong(track);

                    System.out.println(song.getId());

                    String reply = "";
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAYID, song.getId()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                playlistPopup.addRemoveListener(event -> {
                    int track = model.getAnchorSelectionIndex();

                    Song song = playlist.getSong(track);

                    String reply = "";
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlaylistProperties.DELETE_ID, song.getId()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                playlistPopup.show(playlistView.getPlaylist(), e.getX(), e.getY());

            }
        }
    }
}
