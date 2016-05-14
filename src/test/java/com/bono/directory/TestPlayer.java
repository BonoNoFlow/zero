package com.bono.directory;

import com.bono.IdleRunner;
import com.bono.api.*;
import com.bono.controls.CurrentPlaylist;
import com.bono.controls.CurrentSong;
import com.bono.controls.Playback;
import com.bono.view.ControlView;
import com.bono.view.PlaylistView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 27/04/16.
 */
public class TestPlayer {

    private JFrame frame;
    private ControlView controlView;
    private PlaylistView playlistView;

    private Playback playback;
    private PlaylistControl playlistControl;
    private CurrentPlaylist currentPlaylist;
    private CurrentSong currentSong;

    private DBExecutor dbExecutor;

    private Status status;

    public TestPlayer() {
        Config config = new Config("192.168.2.4", 6600);
        dbExecutor = new DBExecutor(config);
        status = new Status(dbExecutor);
        playback = new Playback(dbExecutor, status);
        playlistControl = new PlaylistControl(dbExecutor);
        currentPlaylist = new CurrentPlaylist(playlistControl);
        currentSong = new CurrentSong(playlistControl);
        status.addListener(currentSong);
        updateStatus();

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Test Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            controlView = new ControlView();
            playback.addView(controlView);
            currentSong.addView(controlView);

            IdleRunner idleRunner = new IdleRunner(status);
            idleRunner.addListener(new StatusUpdate());
            idleRunner.start();

            controlView.addNextListener(playback);
            controlView.addStopListener(playback);
            controlView.addPlayListener(playback);
            controlView.addPreviousListener(playback);

            playlistView = new PlaylistView();
            currentPlaylist.setPlaylistView(playlistView);
            currentPlaylist.initPlaylist();

            frame.getContentPane().add(BorderLayout.NORTH, controlView);
            frame.getContentPane().add(BorderLayout.CENTER, playlistView);
            frame.pack();
            frame.setVisible(true);
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

    private class StatusUpdate implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            String line = (String) e.getSource();
            if (line.equals("player")) {
                updateStatus();
                System.out.println(status.getState());
            }
        }

    }


    public static void main(String[] args) {
        new TestPlayer();
    }
}
