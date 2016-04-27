package com.bono.directory;

import com.bono.Idle;
import com.bono.api.*;
import com.bono.controls.Playback;
import com.bono.view.ControlView;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 27/04/16.
 */
public class TestPlayer {

    private JFrame frame;
    private ControlView controlView;

    private Playback playback;

    private DBExecutor dbExecutor;

    private Status status;

    private StatusControl statusControl;

    public TestPlayer() {
        Config config = new Config("192.168.2.4", 6600);
        dbExecutor = new DBExecutor(config);
        status = new Status();
        statusControl = new StatusControl(dbExecutor);
        try {
            status.populateStatus(statusControl.status());
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        playback = new Playback(dbExecutor, status);
        status.addListener(playback);
        Idle idle = new Idle(config, status, statusControl);
        Thread thread = new Thread(idle);
        thread.start();


        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Test Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            controlView = new ControlView();
            playback.addView(controlView);
            controlView.addNextListener(playback);
            controlView.addStopListener(playback);
            controlView.addPlayListener(playback);
            controlView.addPreviousListener(playback);

            frame.getContentPane().add(controlView);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new TestPlayer();
    }
}
