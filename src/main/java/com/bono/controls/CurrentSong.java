package com.bono.controls;

import com.bono.PlaylistPresenter;
import com.bono.api.*;

import com.bono.view.ControlView;

import javax.swing.*;
import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 13/05/16.
 */
public class CurrentSong implements ChangeListener {

    private Song song;



    private PlaylistPresenter playlistPresenter;

    private ControlView controlView;

    public CurrentSong(PlaylistPresenter playlistPresenter) {
        this.playlistPresenter = playlistPresenter;
    }

    public void addView(ControlView controlView)  {
        this.controlView = controlView;
    }

    @Override
    public void stateChanged(EventObject eventObject) {
        Status status = (Status) eventObject.getSource();

        System.out.println("hallo" + status.getState());
        if (status.getSongid() != null) {
            try {
                if (!status.getState().equals(Playback.STOP)) {

                    song = playlistPresenter.song(status.getSongid());
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

    /*
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

    }*/

}
