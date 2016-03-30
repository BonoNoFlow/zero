package com.bono.controls;

import com.bono.MPDStatus;
import com.bono.Utils;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Playlist;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.api.PlayerProperties;
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
    private DBExecutor dbExecutor;
    private MPDStatus status;

    private Playlist playlist;

    public PlaybackController(ControlView controlView, DBExecutor dbExecutor, MPDStatus status, Playlist playlist) {
        this.controlView = controlView;
        this.dbExecutor = dbExecutor;
        this.status = status;
        this.playlist = playlist;
        this.status.addListener(this);
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
        if (!status.getState().equals(null)) {

            if (controlView != null) {

                Utils.Log.print(getClass().getName() + " - " + status.getState());

                switch (status.getState()) {
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

        String reply = "";
        printActionCommand(e.getActionCommand());
        switch (e.getActionCommand()) {
            case PlayerProperties.PREVIOUS:

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PREVIOUS));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                break;
            case PlayerProperties.STOP:

                if (status.getState().equals(PlayerProperties.STOP)) {
                    break;
                }

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.STOP));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                break;
            case PlayerProperties.PAUSE:

                if (status.getState().equals(PlayerProperties.STOP)) {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAY));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (status.getState().equals(PlayerProperties.PAUSE)) {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PAUSE, "0"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PAUSE, "1"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                break;
            case PlayerProperties.NEXT:

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.NEXT));
                } catch (Exception e1) {
                    e1.printStackTrace();
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

        if (!status.getState().equals(PlayerProperties.STOP)) {

            String artist = playlist.getSong(Integer.parseInt(status.getSong())).getArtist();
            String title = playlist.getSong(Integer.parseInt(status.getSong())).getTitle();
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
