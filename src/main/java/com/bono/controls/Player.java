package com.bono.controls;

import com.bono.api.*;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.view.ControlView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 25/04/16.
 */
public class Player implements ActionListener, ChangeListener {

    private ControlView controlView;

    private DBExecutor dbExecutor;

    private PlayerControl playerControl;

    private Status status;

    private Song song;

    public Player(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        playerControl = new PlayerControl(dbExecutor);
    }

    public Player(DBExecutor dbExecutor, Status status) {
        this(dbExecutor);
        this.status = status;

    }

    /*
    Method must be called in event dispatch thread!
     */
    public void initView() {
        controlView = new ControlView();
        controlView.addPreviousListener(this);
        controlView.addStopListener(this);
        controlView.addPlayListener(this);
        controlView.addNextListener(this);
    }

    public void addView(ControlView controlView) {
        this.controlView = controlView;
        this.status.addListener(new PlayerUpdate());
    }

    /*
    Action Listener for the controller buttons.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Playback.PREVIOUS:
                try {
                    playerControl.previous();
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case Playback.STOP:
                try {
                    playerControl.stop();
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case Playback.PAUSE:
                String arg = null;
                 try {
                    if (status.getState().equals(PlayerControl.PAUSE)) {
                        arg = "0";
                    } else if (status.getState().equals(PlayerControl.PLAY)) {
                        arg = "1";
                    } else if (status.getState().equals(PlayerControl.STOP)) {
                        playerControl.play();
                        break;
                    }
                    playerControl.pause(arg);
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case Playback.NEXT:
                try {
                    playerControl.next();
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void stateChanged(EventObject eventObject) {
        String line = (String) eventObject.getSource();
        System.out.println("Idle feedback is: " + line);
        if (line.equals("player")) {
            updateStatus();

        }
    }

    private void updateStatus() {
        String reply = "";
        try {
            status.populateStatus(dbExecutor.execute(new DefaultCommand(Status.STATUS)));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerControl getPlayerControl() {
        return playerControl;
    }

    private class PlayerUpdate implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            Status status = (Status) eventObject.getSource();

            switch (status.getState()) {
                case PlayerControl.STOP:
                    SwingUtilities.invokeLater(() -> {
                        controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                    });
                    break;
                case PlayerControl.PLAY:
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPauseButtonIcon();
                        controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                        icon.setIconHeight(14);
                        icon.setIconWidth(14);
                    });
                    break;
                case PlayerControl.PAUSE:
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