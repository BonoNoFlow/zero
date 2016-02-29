package com.bono.view.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 28/11/15.
 */
public class ControlPanel extends JPanel {

    private PlaybackView playbackView;
    private SongView songView;
    private PlaybackSettings playbackSettings;

    public ControlPanel() {
        super();
        build();
    }

    private void build() {

        playbackView = new PlaybackView();
        songView = new SongView();
        playbackSettings = new PlaybackSettings();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        constraints.weightx = 1.0;

        constraints.anchor = GridBagConstraints.LINE_START;
        add(playbackView, constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        add(songView, constraints);

        constraints.anchor = GridBagConstraints.LINE_END;
        add(playbackSettings, constraints);
    }

    public void addPreviousListener(ActionListener listener) {
        playbackView.addPreviousListener(listener);
    }

    public void addPlayListener(ActionListener listener) {
        playbackView.addPlayListener(listener);
    }

    public void addNextListener(ActionListener listener) {
        playbackView.addNextListener(listener);
    }

    public void addPlaybackSettingsMouseListener(MouseListener listener) {
        playbackSettings.addMouseListener(listener);
    }

    public void setPlayIcon(Icon icon) {
        playbackView.setPlayIcon(icon);
    }

    public void setArtist(String artist) {
        songView.setArtistValue(artist);
    }

    public void setTitle(String title) {
        songView.setTitleValue(title);
    }




}
