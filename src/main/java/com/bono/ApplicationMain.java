package com.bono;

import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.config.Config;
import com.bono.controls.PlaybackController;
import com.bono.models.ServerStatus;
import com.bono.playlist.PlaylistController;
import com.bono.properties.StatusProperties;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationMain {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    private PlaylistController playlistController;

    private PlaybackController playbackController;

    private Config config;

    private DBExecutor dbExecutor;

    private ServerStatus serverStatus;

    public ApplicationMain() {
        init();
        setStatus();
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

    private void setStatus() {
        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand(StatusProperties.STATUS));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    private void build() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView();
            soundcloudController = new SoundcloudController(dbExecutor, applicationView.getSoundcloudView());
            playlistController = new PlaylistController(dbExecutor, applicationView.getPlaylistView());
            //playbackController = new PlaybackController(dbExecutor, applicationView.getControlView(), serverStatus);
            applicationView.show();
        });
    }

    public static void main(String[] args) {
        new ApplicationMain();
    }
}
