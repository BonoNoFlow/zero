package com.bono.controls;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.view.Playback;
import com.bono.view.PlaybackView;

import javax.swing.*;
import java.util.List;

/**
 * Created by bono on 8/11/16.
 */
public class PlaybackPresenter {

    private Playback playback;

    private ClientExecutor clientExecutor;

    private Status status;

    public PlaybackPresenter(ClientExecutor clientExecutor, Status status) {
        this.clientExecutor = clientExecutor;
        this.status = status;
        statusListener();
        songPlaying();
    }

    public void addPlaybackView(Playback playback) {
        this.playback = playback;
        addListeners();
    }

    /*
    Add listeners to the buttons on the playback view.
     */
    private void addListeners() {

        // add previous listener.
        playback.getButtons().get(PlaybackView.PREVIOUS_BUTTON).addButtonActionListener((event) -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.PREVIOUS));
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // add stop listener.
        playback.getButtons().get(PlaybackView.STOP_BUTTON).addButtonActionListener((event) -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.STOP));
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // add play listener.
        playback.getButtons().get(PlaybackView.PLAY_BUTTON).addButtonActionListener((event) -> {
            String arg = null;
            try {
                if (status.getState().equals("pause")) {
                    arg = "0";
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, arg));
                } else if (status.getState().equals("play")) {
                    arg = "1";
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, arg));
                } else if (status.getState().equals("stop")) {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.PLAY));
                }
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // add next listener.
        playback.getButtons().get(PlaybackView.NEXT_BUTTON).addButtonActionListener((event) -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.NEXT));
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // add options listener
        playback.getButtons().get(PlaybackView.OPTIONS_BUTTON).addButtonActionListener((event) -> {
            System.out.println("options");
        });
    }

    private void statusListener() {
        this.status.addListener((eventObject) -> {
            Status status = (Status) eventObject.getSource();

            switch (status.getState()) {
                case "stop":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                        playback.getButtons().get(PlaybackView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                case "play":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPauseButtonIcon();
                        icon.setIconHeight(14);
                        icon.setIconWidth(14);
                        playback.getButtons().get(PlaybackView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                case "pause":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                        playback.getButtons().get(PlaybackView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                default:
                    break;
            }
        });
    }

    private void songPlaying() {
        this.status.addListener((eventObject) -> {
            Status status = (Status) eventObject.getSource();
            final Song song = new Song();
            if (status.getSongid() != null) {

                try {
                    if (!status.getState().equals("stop")) {

                        // TODO exceptions beter opvangen????
                        try {
                            List<String> results =  clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTID, status.getSongid()));
                            song.populate(results);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (playback != null) {
                            SwingUtilities.invokeLater(() -> {
                                playback.setPlayingSong(song.getArtist(), song.getTitle());
                            });
                        }
                    } else {
                        if (playback != null) {
                            SwingUtilities.invokeLater(() -> {
                                playback.setPlayingSong("", "");
                            });
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
