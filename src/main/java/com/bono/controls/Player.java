package com.bono.controls;


import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
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
@Deprecated
public class Player implements ChangeListener {

    private PlaybackController playbackController = new PlaybackController();
    private CurrentSong currentSong = new CurrentSong();

    private ControlView controlView;

    private ClientExecutor clientExecutor;

    private Status status;

    //private Song song;

    public Player(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
    }

    public Player(ClientExecutor clientExecutor, Status status) {
        this(clientExecutor);
        this.status = status;
        this.status.addListener(currentSong);
    }

    /*
    Method must be called in event dispatch thread!
     */
    public void initView() {
        controlView = new ControlView();
        controlView.addPreviousListener(playbackController);
        controlView.addStopListener(playbackController);
        controlView.addPlayListener(playbackController);
        controlView.addNextListener(playbackController);
    }

    public void addView(ControlView controlView) {
        this.controlView = controlView;
        this.status.addListener(new PlayerUpdate());
        this.controlView.addPreviousListener(playbackController);
        this.controlView.addStopListener(playbackController);
        this.controlView.addPlayListener(playbackController);
        this.controlView.addNextListener(playbackController);
    }



    @Override
    public void stateChanged(EventObject eventObject) {
        String line = (String) eventObject.getSource();
        System.out.println("Idle feedback is: " + line);
        if (line.equals("player")) {
            //updateStatus();

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

    /*
    Action Listener class for the controller buttons.
     */
    private class PlaybackController implements ActionListener {
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

    /*
    Display the information of current played song.
     */
    private class CurrentSong implements ChangeListener {

        private Song song;

        @Override
        public void stateChanged(EventObject eventObject) {
            Status status = (Status) eventObject.getSource();
            System.out.println("hallo" + status.getState());
            if (status.getSongid() != null) {
                try {
                    if (!status.getState().equals("stop")) {

                        // TODO exceptions beter opvangen????
                        try {
                            song = new Song(clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTID, status.getSongid())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (controlView != null) {
                            SwingUtilities.invokeLater(() -> {
                                controlView.setArtist(song.getArtist());
                                controlView.setTitle(song.getTitle());
                            });
                        }
                    } else {
                        if (controlView != null) {
                            SwingUtilities.invokeLater(() -> {
                                controlView.setArtist("");
                                controlView.setTitle("");
                            });
                        }
                        song = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
