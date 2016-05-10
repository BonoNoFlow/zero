package com.bono.directory;

import com.bono.Idle;
import com.bono.IdleRunner;
import com.bono.api.*;
import com.bono.controls.Playback;
import com.bono.icons.BonoIconFactory;
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

    private StatusControl statusControl;

    public TestPlayer() {
        Config config = new Config("192.168.2.4", 6600);
        dbExecutor = new DBExecutor(config);
        status = new Status(dbExecutor);
        //status.addListener(playback);
        //statusControl = new StatusControl(dbExecutor);

        playback = new Playback(dbExecutor, status);
        updateStatus();
        //status.addListener(playback);





        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Test Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            controlView = new ControlView();
            playback.addView(controlView);
            IdleRunner idleRunner = new IdleRunner(status);
            idleRunner.addListener(new IdleStatusUpdate());
            //idleRunner.addListener(new IdlePlayerUpdate());
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

    private class IdleStatusUpdate implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            String line = (String) e.getSource();
            if (line.equals("status")) {
                updateStatus();
                System.out.println(status.getState());
            }
        }

    }

    private class IdlePlayerUpdate implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            String line = (String) e.getSource();
            if (line.equals("player")) {
                switch (status.getState()) {
                    case "stop":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                        });
                        break;
                    case "play":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                        });
                        break;
                    case "pause":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new TestPlayer();
    }
}
