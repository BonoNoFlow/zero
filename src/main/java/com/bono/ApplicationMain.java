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

    private MPDStatus mpdStatus;

    public ApplicationMain() {
        init();
        initModels();
        //setStatus();
        build();
        //setStatus();
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

    private void initModels() {
        mpdStatus = new MPDStatus();
        playbackController = new PlaybackController(dbExecutor, mpdStatus);
        mpdStatus.addListener(playbackController);
    }

    private void setStatus() {
        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand(StatusProperties.STATUS));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mpdStatus.setStatus(reply);
        System.out.println(mpdStatus.getState());
    }


    private void build() {
        SwingUtilities.invokeLater(() -> {
            applicationView = new ApplicationView();
            soundcloudController = new SoundcloudController(dbExecutor, applicationView.getSoundcloudView());
            playlistController = new PlaylistController(dbExecutor, applicationView.getPlaylistView());
            playbackController.addControlView(applicationView.getControlView());
            setStatus();
            applicationView.show();
        });
    }

    public static void main(String[] args) {

        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        new ApplicationMain();
    }
}
