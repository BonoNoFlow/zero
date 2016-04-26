package com.bono.controls;

import com.bono.api.ACKException;
import com.bono.api.DBExecutor;
import com.bono.api.PlayerControl;
import com.bono.api.Status;
import com.bono.icons.BonoIconFactory;
import com.bono.view.ControlView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 25/04/16.
 */
public class Playback implements ActionListener, ChangeListener {

    private ControlView controlView;

    private PlayerControl playerControl;

    private Status status;

    public Playback(DBExecutor dbExecutor) {
        playerControl = new PlayerControl(dbExecutor);
    }

    public Playback(DBExecutor dbExecutor, Status status) {
        this(dbExecutor);
        this.status = status;
        this.status.addListener(this);
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

    /*
    Change Listener for the changing of the status object.

    Sets the play button icon.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        status = (Status) e.getSource();
        switch (status.getState()) {
            case PlayerControl.PAUSE:
                SwingUtilities.invokeLater(() -> {
                    controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                });
                break;
            case PlayerControl.PLAY:
                SwingUtilities.invokeLater(() -> {
                    controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                });
                break;
            case PlayerControl.STOP:
                SwingUtilities.invokeLater(() -> {
                    controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                });
                break;
            default:
                break;
        }
    }
}
