package com.bono.controls;

import com.bono.api.*;
import com.bono.view.ControlView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by hendriknieuwenhuis on 13/05/16.
 */
public class CurrentSong implements ChangeListener {

    private Song song;

    private PlaylistControl playlistControl;

    private ControlView controlView;

    public CurrentSong(PlaylistControl playlistControl) {
        this.playlistControl = playlistControl;
    }

    public void addView(ControlView controlView)  {
        this.controlView = controlView;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Status status = (Status) e.getSource();

        System.out.println("hallo" + status.getState());
        try {
            if (!status.getSongid().equals(null) && !status.getState().equals(PlayerControl.STOP)) {
                song = new Song(playlistControl.playlistid(status.getSongid()));
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
