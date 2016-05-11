package com.bono.directory;

import com.bono.IdleRunner;
import com.bono.api.*;
import com.bono.controls.Playback;
import com.bono.view.ControlView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by hendriknieuwenhuis on 27/04/16.
 */
public class TestPlayer {

    private JFrame frame;
    private ControlView controlView;

    private Playback playback;

    private DBExecutor dbExecutor;

    private Status status;

    public TestPlayer() {
        Config config = new Config("192.168.2.4", 6600);
        dbExecutor = new DBExecutor(config);
        status = new Status(dbExecutor);
        playback = new Playback(dbExecutor, status);
        updateStatus();

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Test Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            controlView = new ControlView();
            playback.addView(controlView);
            IdleRunner idleRunner = new IdleRunner(status);
            idleRunner.addListener(new StatusUpdate());
            idleRunner.start();
            controlView.addNextListener(playback);
            controlView.addStopListener(playback);
            controlView.addPlayListener(playback);
            controlView.addPreviousListener(playback);

            frame.getContentPane().add(controlView);
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
