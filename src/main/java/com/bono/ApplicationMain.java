package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.config.Config;
import com.bono.controls.PlaybackController;
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

    private MPDStatus mpdStatus = new MPDStatus();

    private Idle idle;

    public ApplicationMain() {
        init();
        initModels();
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

    private void initModels() {
        playbackController = new PlaybackController(dbExecutor, mpdStatus);
        mpdStatus.addListener(playbackController);
    }

    private void initIdle() {
        idle = new Idle(config, dbExecutor, mpdStatus, playlistController);
        Thread idleThread = new Thread(idle);
        idleThread.start();
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
            initIdle();
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
