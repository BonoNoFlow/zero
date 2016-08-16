package com.bono.controls;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.view.Playback;
import com.bono.view.PlaybackView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        this.status.addListener(playbackStateListener());
        this.status.addListener(currentSongListener());
    }

    public void addPlaybackView(Playback playback) {
        this.playback = playback;
        //addListeners();
        this.playback.getButtons().get(PlaybackView.PREVIOUS_BUTTON).addButtonActionListener(previousButtonListener());
        this.playback.getButtons().get(PlaybackView.STOP_BUTTON).addButtonActionListener(stopButtonListener());
        this.playback.getButtons().get(PlaybackView.PLAY_BUTTON).addButtonActionListener(playButtonListener());
        this.playback.getButtons().get(PlaybackView.NEXT_BUTTON).addButtonActionListener(nextButtonListener());
        this.playback.getButtons().get(PlaybackView.OPTIONS_BUTTON).addButtonActionListener(optionsButtonListener());
    }

    /*
    Add listeners to the buttons on the playback view.
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
    private void addListeners() {

        // add previous listener.
        playback.getButtons().get(PlaybackView.PREVIOUS_BUTTON).addButtonActionListener((event) -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.PREVIOUS));
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();

            }
        });

        // add stop listener.
        playback.getButtons().get(PlaybackView.STOP_BUTTON).addButtonActionListener((event) -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.STOP));
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
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
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // add next listener.
        playback.getButtons().get(PlaybackView.NEXT_BUTTON).addButtonActionListener((event) -> {
            try {
                clientExecutor.execute(new DefaultCommand(MPDPlayback.NEXT));
            } catch (ExecutionException eex) {
                handleExecutionException(eex);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // add options listener
        playback.getButtons().get(PlaybackView.OPTIONS_BUTTON).addButtonActionListener((event) -> {
            System.out.println("options");
        });
    }

    private ChangeListener playbackStateListener() {
        return eventObject -> {
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
        };
    }


    /*
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
    }*/

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
                            song.populate(results);
                        } catch (ExecutionException eex) {
                            handleExecutionException(eex);
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
        };
    }

    /*
    private void songPlaying() {
        this.status.addListener((eventObject) -> {
            Status status = (Status) eventObject.getSource();
            final Song song = new Song();
            if (status.getSongid() != null) {

                try {
                    if (!status.getState().equals("stop")) {

                        // TODO exceptions beter opvangen????
                        try {
                            List<String> results = clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTID, status.getSongid()));
                            song.populate(results);
                        } catch (ExecutionException eex) {
                            handleExecutionException(eex);
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
    }*/

    private void handleExecutionException(ExecutionException eex) {
        if (eex.getCause() instanceof ACKException) {
            JOptionPane.showMessageDialog(null, eex.getCause().getMessage());
        } else {
            eex.printStackTrace();
        }
    }
}
