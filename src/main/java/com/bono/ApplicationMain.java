package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Config;
import com.bono.controls.PlaybackController;
import com.bono.directory.MPDDirectory;
import com.bono.api.StatusProperties;
import com.bono.playlist.PlaylistController;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationMain {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    private PlaybackController playbackController;

    // refactor - rename!
    private PlaylistController playlistController;

    private Config config;

    private DBExecutor dbExecutor;

    private MPDStatus mpdStatus;

    private MPDDirectory directory;

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
        // TODO params moeten ook getest worden.


        try {
            config.loadParams();
        } catch (Exception e) {

            //ConfigOptions configOptions = new ConfigOptions(config);
        } finally {
            config.setHost("192.168.2.4");
            config.setPort(6600);
            dbExecutor = new DBExecutor(config);
        }

        /*
        // runs in swing thread.
        ConfigOptions configOptions;
        try {
            configOptions = new ConfigOptions(config);
            //configOptions.currentThread().join();
            //Thread[] threads = Thread.
            //SwingUtilities.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // thread joined.
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/


        dbExecutor = new DBExecutor(config);
    }


    private void initIdle() {
        idle = new Idle(config, dbExecutor, mpdStatus);
        idle.addListener(playlistController.getIdlePlaylistListener());
        Thread idleThread = new Thread(idle);
        idleThread.start();
    }

    private void initModels() {
        mpdStatus = new MPDStatus();

        playlistController = new PlaylistController();
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
            directory = new MPDDirectory(dbExecutor, applicationView.getDirectoryView());
            soundcloudController = new SoundcloudController(dbExecutor, applicationView.getSoundcloudView());

            applicationView.getDirectoryView().getDirectory().addMouseListener(directory);
            applicationView.getDirectoryView().getDirectory().addTreeWillExpandListener(directory);

            setStatus();
            playbackController = new PlaybackController(dbExecutor, mpdStatus);
            playbackController.addControlView(applicationView.getControlView());

            playlistController.setDbExecutor(dbExecutor);
            playlistController.setPlaylistView(applicationView.getPlaylistView());
            playlistController.init();
            initIdle();
            applicationView.show();
        });
    }

    public static void main(String[] args) {
        new ApplicationMain();
    }
}
