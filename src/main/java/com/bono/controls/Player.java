package com.bono.controls;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDStatus;
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



    private ClientExecutor clientExecutor;

    private Status status;

    private Song song;


    public Player(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;

    }

    public Player(ClientExecutor clientExecutor, Status status) {
        this(clientExecutor);
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
            case MPDPlayback.PREVIOUS:
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.PREVIOUS));
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case MPDPlayback.STOP:
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.STOP));
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case MPDPlayback.PAUSE:
                String arg = null;
                 try {
                    if (status.getState().equals("pause")) {
                        arg = "0";
                    } else if (status.getState().equals("play")) {
                        arg = "1";
                    } else if (status.getState().equals("stop")) {
                        clientExecutor.execute(new DefaultCommand(MPDPlayback.PLAY));
                        break;
                    }
                     clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, arg));
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case MPDPlayback.NEXT:
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.NEXT));
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
            status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class PlayerUpdate implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            Status status = (Status) eventObject.getSource();

            switch (status.getState()) {
                case "stop":
                    SwingUtilities.invokeLater(() -> {
                        controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                    });
                    break;
                case "play":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPauseButtonIcon();
                        controlView.setPlayIcon(BonoIconFactory.getPauseButtonIcon());
                        icon.setIconHeight(14);
                        icon.setIconWidth(14);
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
