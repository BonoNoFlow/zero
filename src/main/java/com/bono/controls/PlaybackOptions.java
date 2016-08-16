package com.bono.controls;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.view.PlaybackOptionsView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Created by bono on 8/16/16.
 */
public class PlaybackOptions {

    private PlaybackOptionsView playbackOptionsView;

    private ClientExecutor clientExecutor;

    private Status status;

    private StatusListener statusListener = new StatusListener();

    public PlaybackOptions(ClientExecutor clientExecutor, Status status) {
        this.clientExecutor = clientExecutor;
        this.status = status;
        this.status.addListener(statusListener);
        SwingUtilities.invokeLater(() -> {
            playbackOptionsView = new PlaybackOptionsView();
            playbackOptionsView.addApplyListener(applyListener());
            playbackOptionsView.addCloseListener(closeListener());
            setButtons(status);
            playbackOptionsView.show();
        });

    }

    private void setButtons(Status status) {
        if (status != null) {
            if (status.getRepeat().equals("0")) {
                playbackOptionsView.getRepeatModel().setSelected(false);
            } else {
                playbackOptionsView.getRepeatModel().setSelected(true);
            }

            if (status.getSingle().equals("0")) {
                playbackOptionsView.getSingleModel().setSelected(false);
            } else {
                playbackOptionsView.getSingleModel().setSelected(true);
            }

            if (status.getRandom().equals("0")) {
                playbackOptionsView.getRandomModel().setSelected(false);
            } else {
                playbackOptionsView.getRandomModel().setSelected(true);
            }

            if (status.getConsume().equals("0")) {
                playbackOptionsView.getConsumeModel().setSelected(false);
            } else {
                playbackOptionsView.getConsumeModel().setSelected(true);
            }
        }
    }

    private ActionListener applyListener() {
        return event -> {
          System.out.println("apply");
            boolean repeat = playbackOptionsView.getRepeatModel().isSelected();
            boolean single = playbackOptionsView.getSingleModel().isSelected();
            boolean random = playbackOptionsView.getRandomModel().isSelected();
            boolean consume = playbackOptionsView.getConsumeModel().isSelected();

            List<Command> commands = new ArrayList<>();
            commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_BEGIN));

            // repeat
            if (repeat) {
                System.out.println("Repeat: " + repeat);
                commands.add(new DefaultCommand(MPDPlayback.REPEAT, "1"));
            } else {
                commands.add(new DefaultCommand(MPDPlayback.REPEAT, "0"));
            }

            // single
            if (single) {
                System.out.println("Single: " + single);
                commands.add(new DefaultCommand(MPDPlayback.SINGLE, "1"));
            } else {
                commands.add(new DefaultCommand(MPDPlayback.SINGLE, "0"));
            }

            // random
            if (random) {
                System.out.println("Random: " + random);
                commands.add(new DefaultCommand(MPDPlayback.RANDOM, "1"));
            } else {
                commands.add(new DefaultCommand(MPDPlayback.RANDOM, "0"));
            }

            // consume
            if (consume) {
                System.out.println("Consume: " + consume);
                commands.add(new DefaultCommand(MPDPlayback.CONSUME, "1"));
            } else {
                commands.add(new DefaultCommand(MPDPlayback.CONSUME, "0"));
            }

            commands.add(new DefaultCommand(DefaultCommand.COMMAND_LIST_END));

            try {
                clientExecutor.executeList(commands);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }



    private ActionListener closeListener() {
        return event -> {
            if (status.removeListener(statusListener)) {
                playbackOptionsView.close();
            }
        };
    }

    /*
    Listener as inner class because it has to be removed from the status object
    when the view is closed!
     */
    private class StatusListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            Status status = (Status) eventObject.getSource();
            setButtons(status);
        }
    }
}
