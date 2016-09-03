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
 *
 * TODO Misschien oude waarden registreren en allen veranderde waarden versturen ipv alles.
 */
public class PlaybackOptions {

    private PlaybackOptionsView playbackOptionsView;

    private ClientExecutor clientExecutor;

    private Player player;

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

    public PlaybackOptions(Player player, Status status) {
        this.player = player;
        this.clientExecutor = player.getClientExecutor();
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
            if (status.isRepeat()) {
                playbackOptionsView.getRepeatModel().setSelected(true);
            } else {
                playbackOptionsView.getRepeatModel().setSelected(false);
            }

            if (status.isSingle()) {
                playbackOptionsView.getSingleModel().setSelected(true);
            } else {
                playbackOptionsView.getSingleModel().setSelected(false);
            }

            if (status.isRandom()) {
                playbackOptionsView.getRandomModel().setSelected(true);
            } else {
                playbackOptionsView.getRandomModel().setSelected(false);
            }

            if (status.isConsume()) {
                playbackOptionsView.getConsumeModel().setSelected(true);
            } else {
                playbackOptionsView.getConsumeModel().setSelected(false);
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

            /*
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
            }*/
            // repeat
            commands.add(new DefaultCommand(Player.REPEAT, Player.booleanString(repeat)));
            // single
            commands.add(new DefaultCommand(Player.SINGLE, Player.booleanString(single)));
            // random
            commands.add(new DefaultCommand(Player.RANDOM, Player.booleanString(random)));
            // consume
            commands.add(new DefaultCommand(Player.CONSUME, Player.booleanString(consume)));

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
