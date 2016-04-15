package com.bono.controls;

import com.bono.api.MPDStatus;
import com.bono.Utils;
import com.bono.api.*;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.view.ControlView;
import com.bono.view.PlaybackPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 01/03/16.
 */
public class PlaybackController implements ActionListener, ChangeListener {

    private ControlView controlView;
    private MPDStatus status;

    private Playlist playlist;

    private Player player;

    public PlaybackController(ControlView controlView, DBExecutor dbExecutor, MPDStatus status, Playlist playlist) {
        this.controlView = controlView;
        this.player = new Player(dbExecutor);
        this.status = status;
        this.playlist = playlist;
        this.controlView.addPreviousListener(this);
        this.controlView.addStopListener(this);
        this.controlView.addPlayListener(this);
        this.controlView.addNextListener(this);
        init(status);
    }

    public void addControlView(ControlView controlView) {
        this.controlView = controlView;
        this.controlView.addPreviousListener(this);
        this.controlView.addStopListener(this);
        this.controlView.addPlayListener(this);
        this.controlView.addNextListener(this);

    }

    private void init(MPDStatus status) {
        if (!status.getStatus().getState().equals(null)) {

            if (controlView != null) {

                Utils.Log.print(getClass().getName() + " - " + status.getStatus().getState());

                switch (status.getStatus().getState()) {
                    case "play":
                        SwingUtilities.invokeLater(() -> {
                            BonoIcon icon = BonoIconFactory.getPauseButtonIcon();
                            icon.setIconHeight(PlaybackPanel.ICON_HEIGHT);
                            icon.setIconWidth(PlaybackPanel.ICON_WIDTH);
                            controlView.setPlayIcon(icon);
                        });
                        break;
                    case "stop":
                        SwingUtilities.invokeLater(() -> {
                            BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                            icon.setIconHeight(PlaybackPanel.ICON_HEIGHT);
                            icon.setIconWidth(PlaybackPanel.ICON_WIDTH);
                            controlView.setPlayIcon(icon);
                        });
                        break;
                    case "pause":
                        SwingUtilities.invokeLater(() -> {
                            BonoIcon icon = BonoIconFactory.getPlayButtonIcon();
                            icon.setIconHeight(PlaybackPanel.ICON_HEIGHT);
                            icon.setIconWidth(PlaybackPanel.ICON_WIDTH);
                            controlView.setPlayIcon(icon);
                        });
                        break;
                    default:
                        //Utils.Log.print(getClass().getName() + " " + status.getState());
                        break;
                }
            }
        }
    }

    /*
    ActionListener, listens to the playback controls

     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //String reply = "";
        printActionCommand(e.getActionCommand());
        switch (e.getActionCommand()) {
            case Player.PREVIOUS:
                try {
                    player.previous();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Player.STOP:
                if (status.getStatus().getState().equals(Player.STOP)) {
                    break;
                }
                try {
                    player.stop();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Player.PAUSE:
                if (status.getStatus().getState().equals(Player.STOP)) {
                    try {
                        player.play();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                } else if (status.getStatus().getState().equals(Player.PAUSE)) {
                    try {
                        player.pause("0");
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                } else {
                    try {
                        player.pause("1");
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            case Player.NEXT:
                try {
                    player.next();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                //Utils.Log.print(e.getActionCommand());
                break;
        }
    }

    /*
    ChangeListener listens to the state of the server.

    When the state is changed the icon on the
    play/pause button is set.

    .1 'play' the pause icon is set.

    .2 'stop the play icon is set.

    .3 'pause' the play icon is set.

    Also the song that is playing is set!
     */
    @Override
    public void stateChanged(ChangeEvent e) {

        MPDStatus status = (MPDStatus) e.getSource();

        init(status);

        if (!status.getStatus().getState().equals(Player.STOP)) {

            String artist = playlist.getSong(Integer.parseInt(status.getStatus().getSong())).getArtist();
            String title = playlist.getSong(Integer.parseInt(status.getStatus().getSong())).getTitle();
            SwingUtilities.invokeLater(() -> {

                controlView.setArtist(artist);
                controlView.setTitle(title);

            });
        } else {
            SwingUtilities.invokeLater(() -> {

                controlView.setArtist("");
                controlView.setTitle("");

            });
        }

    }

    private void printActionCommand(String value) {
        Utils.Log.print(getClass().getName() + " - ActionCommand: " + value);
    }
}
