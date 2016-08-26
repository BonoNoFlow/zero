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

    private Playlist playlist;

    public PlaybackPresenter(ClientExecutor clientExecutor, Status status, Playlist playlist) {
        this.clientExecutor = clientExecutor;
        this.playlist = playlist;
        this.status = status;
        this.status.addListener(playbackStateListener());
        this.status.addListener(currentSongListener());
        this.playbackScrolleController = new PlaybackScrolleController(clientExecutor, playlist);
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
                    //clientExecutor.execute(new DefaultCommand(MPDPlayback.SETVOL, Integer.toString(value)));
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
                if (status.getState() == Status.PAUSE_STATE) {
                    arg = "0";
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, arg));
                } else if (status.getState() == Status.PLAY_STATE) {
                    arg = "1";
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, arg));
                } else if (status.getState() == Status.STOP_STATE) {
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
                case Status.STOP_STATE:
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                        playbackView.getButtons().get(PlaybackControlsView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                case Status.PLAY_STATE:
                    SwingUtilities.invokeLater(() -> {
                        BonoIcon icon = BonoIconFactory.getPauseButtonIcon();
                        icon.setIconHeight(14);
                        icon.setIconWidth(14);
                        playbackView.getButtons().get(PlaybackControlsView.PLAY_BUTTON).setButtonIcon(icon);
                    });
                    break;
                case Status.PAUSE_STATE:
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

    // TODO current song controller
    private ChangeListener currentSongListener() {
        return eventObject -> {

            Status status = (Status) eventObject.getSource();

            if (status.getSongid() != -1) {

                try {
                    if (status.getState() != Status.STOP_STATE) {

                        Song song = playlist.getSong(status.getSong());

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
