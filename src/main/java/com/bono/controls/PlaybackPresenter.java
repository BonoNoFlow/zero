package com.bono.controls;

import com.bono.api.*;
import com.bono.api.ChangeListener;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.PlaybackView;
import com.bono.view.PlaybackControlsView;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by bono on 8/11/16.
 */
public class PlaybackPresenter {

    private PlaybackView playbackView;

    private PlaybackScrolleController playbackScrolleController;

    private ClientExecutor clientExecutor;

    private Status status;

    public PlaybackPresenter(ClientExecutor clientExecutor, Status status) {
        this.clientExecutor = clientExecutor;
        this.status = status;
        this.status.addListener(playbackStateListener());
        this.status.addListener(currentSongListener());
        this.playbackScrolleController = new PlaybackScrolleController(clientExecutor);
    }

    public void addPlaybackView(PlaybackControlsView playbackControlsView) {
        this.playbackView = playbackControlsView;
        //addListeners();
        this.playbackView.getButtons().get(PlaybackControlsView.PREVIOUS_BUTTON).addButtonActionListener(previousButtonListener());
        this.playbackView.getButtons().get(PlaybackControlsView.STOP_BUTTON).addButtonActionListener(stopButtonListener());
        this.playbackView.getButtons().get(PlaybackControlsView.PLAY_BUTTON).addButtonActionListener(playButtonListener());
        this.playbackView.getButtons().get(PlaybackControlsView.NEXT_BUTTON).addButtonActionListener(nextButtonListener());
        this.playbackView.getButtons().get(PlaybackControlsView.OPTIONS_BUTTON).addButtonActionListener(optionsButtonListener());
        this.playbackView.getVolume().addChangeListener(volumeButtonListener());
        this.playbackView.getVolume().setVolume(status.getVolume());
        this.playbackScrolleController.addScrollerView(playbackControlsView);
        status.addListener(this.playbackScrolleController);
        //this.playbackScrolleController.resetScroller(status);
    }

    private javax.swing.event.ChangeListener volumeButtonListener() {
        return e -> {
            JSlider s = (JSlider) e.getSource();
            if (!s.getValueIsAdjusting()) {
                int value = s.getValue();
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.SETVOL, Integer.toString(value)));
                } catch (ExecutionException eex) {
                    handleExecutionException(eex);
                } catch (Exception ioe) {
                    ioe.printStackTrace();
                }
            }
        };
    }

    /*
    Add listeners to the buttons on the playbackView view.
     */

    // previuos button listener.
    private ActionListener previousButtonListener() {
        return event -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.PREVIOUS));
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        };
    }

    // stop button listener.
    private ActionListener stopButtonListener() {
        return event -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.STOP));
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        };
    }

    // play button listener.
    private ActionListener playButtonListener() {
        return event -> {
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
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        };
    }

    // next button listener.
    private ActionListener nextButtonListener() {
        return event -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.NEXT));
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        };
    }

    // options button listener
    private ActionListener optionsButtonListener() {
        return event -> {
            new PlaybackOptions(clientExecutor, status);

        };
    }


    private ChangeListener playbackStateListener() {
        return eventObject -> {
            Status status = (Status) eventObject.getSource();
            playbackView.getVolume().setVolume(status.getVolume());
            switch (status.getState()) {
                case "stop":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                        playbackView.getButtons().get(PlaybackControlsView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                case "play":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPauseButtonIcon();
                        icon.setIconHeight(14);
                        icon.setIconWidth(14);
                        playbackView.getButtons().get(PlaybackControlsView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                case "pause":
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                        playbackView.getButtons().get(PlaybackControlsView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                default:
                    break;
            }
        };
    }

    private ChangeListener currentSongListener() {
        return eventObject -> {
            Status status = (Status) eventObject.getSource();
            final Song song = new Song();
            if (status.getSongid() != null) {

                try {
                    if (!status.getState().equals("stop")) {

                        // TODO exceptions beter opvangen????
                        try {
                            List<String> results = clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTID, status.getSongid()));

                            // TODO maybe just a link to the playlist instead of a listener.
                            song.addListener(new SoundcloudController(clientExecutor));

                            song.populate(results);
                        } catch (ExecutionException eex) {
                            handleExecutionException(eex);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (playbackView != null) {
                            SwingUtilities.invokeLater(() -> {
                                playbackView.setPlayingSong(song.getArtist(), song.getTitle());
                            });
                        }
                    } else {
                        if (playbackView != null) {
                            SwingUtilities.invokeLater(() -> {
                                playbackView.setPlayingSong("", "");
                            });
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }



    private void handleExecutionException(ExecutionException eex) {
        if (eex.getCause() instanceof ACKException) {
            JOptionPane.showMessageDialog(null, eex.getCause().getMessage());
        } else {
            eex.printStackTrace();
        }
    }
}
