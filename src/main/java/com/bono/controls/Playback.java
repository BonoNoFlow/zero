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
public class Playback implements ActionListener, ChangeListener {

    private ControlView controlView;

    private DBExecutor dbExecutor;

    private PlayerControl playerControl;

    private Status status;

    private Song song;

    public Playback(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        playerControl = new PlayerControl(dbExecutor);
    }

    public Playback(DBExecutor dbExecutor, Status status) {
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
            case PlayerControl.PREVIOUS:
                try {
                    playerControl.previous();
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case PlayerControl.STOP:
                try {
                    playerControl.stop();
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case PlayerControl.PAUSE:
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
            case PlayerControl.NEXT:
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
            System.out.println("Playback private class: StatusUpdate " + status.getState());
        }
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

    public PlayerControl getPlayerControl() {
        return playerControl;
    }

    private class IdleStatusUpdate implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            String line = (String) eventObject.getSource();
            if (line.equals("status")) {
                updateStatus();
                System.out.println(status.getState());
            }
        }

    }

    private class PlayerUpdate implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            Status status = (Status) eventObject.getSource();

            switch (status.getState()) {
                case PlayerControl.STOP:
                    System.out.println(status.getState());
                    SwingUtilities.invokeLater(() -> {
                        controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                    });
                    break;
                case PlayerControl.PLAY:
                    System.out.println(status.getState());
                    SwingUtilities.invokeLater(() -> {
                        controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                    });
                    break;
                case PlayerControl.PAUSE:
                    System.out.println("Status update: " + status.getState());
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                        icon.setIconHeight(20);
                        icon.setIconWidth(20);
                        controlView.setPlayIcon(icon);
                    });
                    break;
                default:
                    break;
            }
        }

    }

}
