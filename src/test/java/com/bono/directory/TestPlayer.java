package com.bono.directory;

import com.bono.IdleRunner;
import com.bono.api.*;
import com.bono.controls.CurrentPlaylist;
import com.bono.controls.CurrentSong;
import com.bono.controls.Playback;
import com.bono.view.ApplicationView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 27/04/16.
 */

// TODO Status verandering current song listener doet het niet.
// TODO
public class TestPlayer extends WindowAdapter {

    private ApplicationView applicationView;

    private Playback playback;
    private PlaylistControl playlistControl;
    private CurrentPlaylist currentPlaylist;
    private CurrentSong currentSong;

    private DBExecutor dbExecutor;

    private Status status;

    private IdleRunner idleRunner;

    public TestPlayer() {
        Config config = new Config("192.168.2.4", 6600);
        dbExecutor = new DBExecutor(config);
        status = new Status(dbExecutor);
        playback = new Playback(dbExecutor, status);
        playlistControl = new PlaylistControl(dbExecutor);
        currentPlaylist = new CurrentPlaylist(playlistControl, playback);
        currentSong = new CurrentSong(playlistControl);
        status.addListener(currentSong);

        updateStatus();

        SwingUtilities.invokeLater(() -> {

            applicationView = new ApplicationView(initFrameDimension(), this);


            playback.addView(applicationView.getControlView());
            currentSong.addView(applicationView.getControlView());


            applicationView.getControlView().addNextListener(playback);
            applicationView.getControlView().addStopListener(playback);
            applicationView.getControlView().addPlayListener(playback);
            applicationView.getControlView().addPreviousListener(playback);


            applicationView.getPlaylistView().addMouseListener(currentPlaylist);
            currentPlaylist.setPlaylistView(applicationView.getPlaylistView());
            currentPlaylist.initPlaylist();

            // directory implementen

            applicationView.show();
            System.out.println("Aplication gets shown");
        });
    }

    private void updateStatus() {
        try {
            status.populate();
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // setting the  frame dimension.
    private Dimension initFrameDimension() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = (graphicsDevice.getDisplayMode().getWidth() * 0.8);
        double height = (graphicsDevice.getDisplayMode().getHeight() * 0.8);
        return new Dimension((int) width, (int)height);
    }


    // Adapter stuff
    private boolean closing = false;

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        //System.out.println("windowOpened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        System.out.println("windowClosing");

        closing = true;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        //System.out.println("windowClosed");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        super.windowIconified(e);
        //System.out.println("windowIconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        super.windowDeiconified(e);
        //System.out.println("windowDeiconified");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        super.windowActivated(e);
        System.out.println("windowActivated");

        idleRunner = new IdleRunner(status);
        //idleRunner.addListener(new StatusUpdate());
        idleRunner.addListener(playback);
        idleRunner.addListener(currentPlaylist);
        idleRunner.start();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        super.windowDeactivated(e);
        System.out.println("windowDeactivated");

        // kan ook in iconified!!!!
        idleRunner.removeListeners();
        idleRunner = null;
        /// !!!!!!!!!!!!!!!!!!!!!!!

        if (closing) {
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        new TestPlayer();
    }
}
