package com.bono.controls;

import com.bono.api.*;
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
public class Playback implements ActionListener {

    private ControlView controlView;

    private PlayerControl playerControl;

    private Status status;

    private Song song;

    public Playback(DBExecutor dbExecutor) {
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

    private class PlayerUpdate implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            Status status = (Status) e.getSource();

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
                    System.out.println(status.getState());
                    SwingUtilities.invokeLater(() -> {
                        controlView.setPlayIcon(BonoIconFactory.getPlayButtonIcon());
                    });
                    break;
                default:
                    break;
            }
        }
    }

    /*
    Listener to Status for song change. It sets the current song view
    to display the current playing song.
    */
    private class CurrentSong implements ChangeListener {

        private DBExecutor dbExecutor;

        public CurrentSong(DBExecutor dbExecutor) {
            this.dbExecutor = dbExecutor;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Status status = (Status) e.getSource();
            PlaylistControl playlistControl = new PlaylistControl(dbExecutor);

            try {
                song = new Song(playlistControl.playlistid(status.getSongid()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                controlView.setArtist(song.getArtist());
                controlView.setTitle(song.getTitle());
            });
        }
    }

}
