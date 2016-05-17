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

    private void updateView() {
        if (controlView != null) {
            SwingUtilities.invokeLater(() -> {
                if (song != null) {
                    controlView.setArtist(song.getArtist());
                    controlView.setTitle(song.getTitle());
                } else {
                    controlView.setArtist("");
                    controlView.setTitle("");
                }
            });
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Status status = (Status) e.getSource();

        try {
            if (status.getSongid() != null && !status.getState().equals(PlayerControl.STOP)) {
                song = new Song();
                String current = playlistControl.playlistid(status.getSongid());
                System.out.println(current);
            } else {
                song = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        updateView();
    }

}
