package com.bono;

import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.config.Config;
import com.bono.view.ConfigOptionsView;
import com.bono.playlist.Playlist;
import com.bono.playlist.PlaylistProperties;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationMain {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    private Config config;

    private Playlist playlist;

    private DBExecutor dbExecutor;
    public ApplicationMain() {
        init();
        loadPlaylist();
        build();
    }

    private void init() {
        config = new Config();
        // dit moet vervangen worden met loadparams in try catch!
        // TODO verschillende methods!!
        // TODO params moeten ook getsets worden.
        try {
            config.loadParams();
        } catch (Exception e) {
            //ConfigOptionsView configOptionsView = new ConfigOptionsView(config);
            //config.loadParams();
        } finally {
            config.setHost("192.168.2.4");
            config.setPort(6600);
            dbExecutor = new DBExecutor(config);
        }


    }

    private void loadPlaylist() {
        playlist = new Playlist();
        try {
            playlist.populate(dbExecutor.execute(new MPDCommand(PlaylistProperties.PLAYLISTINFO)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        playlist.printPlaylist();
    }

    private void build() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView();
            soundcloudController = new SoundcloudController(dbExecutor, applicationView.getSoundcloudView());
            applicationView.getPlaylistView().setModel(playlist.getPlaylistModel());
            applicationView.view();
        });
    }

    public static void main(String[] args) {
        new ApplicationMain();
    }
}
