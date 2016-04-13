package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.DefaultCommand;
//import com.bono.api.MPDCommand;
import com.bono.api.Config;
import com.bono.controls.PlaybackController;
import com.bono.directory.MPDDirectory;
import com.bono.api.StatusProperties;
import com.bono.playlist.PlaylistController;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.ApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationMain extends WindowAdapter {

    private ApplicationView applicationView;

    private SoundcloudController soundcloudController;

    private PlaybackController playbackController;

    // refactor - rename!
    private PlaylistController playlistController;

    private Config config;

    private DBExecutor dbExecutor;

    //private MPDStatus mpdStatus;
    private com.bono.api.MPDStatus mpdStatus;

    private MPDDirectory directory;

    private Idle idle;

    Dimension frameDimension;

    public ApplicationMain() {
        init();
        initModels();
        setStatus();
        build();
    }

    private void initFrameDimension() {
        // setting the  frame dimension.
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = (graphicsDevice.getDisplayMode().getWidth() * 0.8);
        double height = (graphicsDevice.getDisplayMode().getHeight() * 0.8);
        frameDimension =  new Dimension((int) width, (int)height);
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


        //dbExecutor = new DBExecutor(config);
    }


    private void initIdle() {
        //idle = new Idle(config, dbExecutor, mpdStatus);
        idle.addListener(playlistController.getIdlePlaylistListener());
        //idle.addListener(mpdStatus);
        Thread idleThread = new Thread(idle);
        idleThread.start();
    }

    private void initModels() {
        //mpdStatus = new MPDStatus(dbExecutor);

        playlistController = new PlaylistController(dbExecutor);
    }

    private void setStatus() {
        String reply = "";
        try {
            reply = dbExecutor.execute(new DefaultCommand(StatusProperties.STATUS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mpdStatus.setStatus(reply);
        //System.out.println(mpdStatus.getState());
    }

    private void build() {
        SwingUtilities.invokeLater(() -> {
            initFrameDimension();
            applicationView = new ApplicationView(frameDimension, this);
            directory = new MPDDirectory(dbExecutor, applicationView.getDirectoryView());
            soundcloudController = new SoundcloudController(dbExecutor, applicationView.getSoundcloudView());

            applicationView.getDirectoryView().getDirectory().addMouseListener(directory);
            applicationView.getDirectoryView().getDirectory().addTreeWillExpandListener(directory);

            //playlistController.setDbExecutor(dbExecutor);
            playlistController.setPlaylistView(applicationView.getPlaylistView());
            playlistController.init();

            //playbackController = new PlaybackController(applicationView.getControlView(), dbExecutor, mpdStatus, playlistController.getPlaylist());
            //playbackController.addControlView(applicationView.getControlView());
            mpdStatus.addListener(playbackController);

            applicationView.show();
        });
    }


    /*
    WindowAdapter.


     */

    boolean iconified = false;

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        Utils.Log.print(getClass().getName()+" window opened.");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        Utils.Log.print(getClass().getName()+" window closing.");
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        Utils.Log.print(getClass().getName()+" window closed.");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        super.windowIconified(e);
        iconified = true;
        Utils.Log.print(getClass().getName()+" window iconified.");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        super.windowDeiconified(e);
        Utils.Log.print(getClass().getName()+" window deiconified.");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        super.windowActivated(e);
        Utils.Log.print(getClass().getName()+" window activated.");
        initIdle();
    }

    /*
    Exit the application.
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);
        Utils.Log.print(getClass().getName()+" window deactivated.");
        //if (iconified) {
        //    Utils.Log.print("iconified");
        //    return;
        //}
        //System.exit(0);
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        super.windowStateChanged(e);
        Utils.Log.print(getClass().getName()+" window state changed.");
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        super.windowGainedFocus(e);
        Utils.Log.print(getClass().getName()+" window gained focus.");
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        super.windowLostFocus(e);
        Utils.Log.print(getClass().getName()+" window lost focus.");
    }

    public static void main(String[] args) {
        new ApplicationMain();
    }
}
