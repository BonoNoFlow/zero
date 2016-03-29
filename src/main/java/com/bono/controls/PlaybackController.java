package com.bono.controls;

import com.bono.MPDStatus;
import com.bono.Utils;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.icons.BonoIconFactory;
import com.bono.api.PlayerProperties;
import com.bono.view.ControlView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 01/03/16.
 */
public class PlaybackController implements ActionListener, ChangeListener {

    private ControlView controlView;
    private DBExecutor dbExecutor;
    private MPDStatus status;

    public PlaybackController(DBExecutor dbExecutor, MPDStatus status) {
        this.dbExecutor = dbExecutor;
        this.status = status;
        this.status.addListener(this);
        init(status);
    }

    public void addControlView(ControlView controlView) {
        this.controlView =controlView;
        this.controlView.addPreviousListener(this);
        this.controlView.addStopListener(this);
        this.controlView.addPlayListener(this);
        this.controlView.addNextListener(this);

    }

    private void init(MPDStatus status) {
        if (status.getState() != null) {

            if (controlView != null) {

                switch (status.getState()) {
                    case "play":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                        });
                        //break;
                    case "stop":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                        });
                        //break;
                    case "pause":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                        });
                        //break;
                    default:
                        Utils.Log.print(getClass().getName() + " " + status.getState());
                        break;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String reply = "";

        switch (e.getActionCommand()) {
            case PlayerProperties.PREVIOUS:
                //printActionCommand(e.getActionCommand());

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PREVIOUS));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //System.out.println(reply);
                //break;
            case PlayerProperties.STOP:
                if (status.getState().equals(PlayerProperties.STOP)) {
                    break;
                }
                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.STOP));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //break;
            case PlayerProperties.PAUSE:
                if (status.getState().equals(PlayerProperties.STOP)) {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAY));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (status.getState().equals(PlayerProperties.PAUSE)) {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PAUSE, "0"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PAUSE, "1"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                //printActionCommand(e.getActionCommand());
                //break;
            case PlayerProperties.NEXT:

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.NEXT));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //System.out.println(reply);
                //printActionCommand(e.getActionCommand());
                //break;
            default:
                Utils.Log.print(e.getActionCommand());
                break;
        }
    }

    /*
    ChangeListener listens to the state of the server.

    When the state is changed the icon on the
    play/pause button is set.

    .1 'play' the pause icon is set.

    .2 'stop the play icon is set.

    .3 'pause' the play icon is set.
     */
    @Override
    public void stateChanged(ChangeEvent e) {


        init(((MPDStatus) e.getSource()));
        /*
        if (status.getState() != null) {

            if (controlView != null) {

                switch (status.getState()) {
                    case "play":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                        });
                        break;
                    case "stop":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                        });
                        break;
                    case "pause":
                        SwingUtilities.invokeLater(() -> {
                            controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                        });
                        break;
                    default:
                        Utils.Log.print(getClass().getName() + " " + status.getState());
                        break;
                }
            }
        }*/
    }

    private void printActionCommand(String value) {
        System.out.println("ActionCommand: " + value);
    }
}
