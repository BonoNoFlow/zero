package com.bono.database;

import com.bono.IdleRunner;
import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;
import com.bono.controls.PlaybackPresenter;
import com.bono.view.PlaybackControlsView;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

/**
 * Created by bono on 8/11/16.
 */
public class TestControls {

    Status status = new Status();

    ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);

    PlaybackPresenter playbackPresenter;

    public TestControls() {

        IdleRunner idleRunner = new IdleRunner(clientExecutor);
        idleRunner.addListener(new StatusUpdater());
        idleRunner.start();
        playbackPresenter = new PlaybackPresenter(clientExecutor, status, null);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel();
            PlaybackControlsView playbackControlsView = new PlaybackControlsView();
            playbackPresenter.addPlaybackView(playbackControlsView);
            //controlButton.setBorder(null);
            //controlButton.setBounds(0,0,0,0);

            frame.getContentPane().add(playbackControlsView, BorderLayout.NORTH);
            frame.pack();
            frame.setVisible(true);
        });
        updateStatus();
    }

    private void updateStatus() {
        try {
            status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class StatusUpdater implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            updateStatus();
        }
    }

    public static void main(String[] args) {
        new TestControls();
    }
}