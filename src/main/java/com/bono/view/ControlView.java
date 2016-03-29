package com.bono.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 28/11/15.
 */
public class ControlView extends JPanel {

    private PlaybackPanel playbackPanel;
    private SongPanel songPanel;
    private PlaybackSettingsPanel playbackSettingsPanel;

    public ControlView() {
        super();
        build();
    }

    private void build() {

        playbackPanel = new PlaybackPanel();
        songPanel = new SongPanel();
        playbackSettingsPanel = new PlaybackSettingsPanel();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        constraints.weightx = 1.0;

        constraints.anchor = GridBagConstraints.LINE_START;
        add(playbackPanel, constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        add(songPanel, constraints);

        constraints.anchor = GridBagConstraints.LINE_END;
        add(playbackSettingsPanel, constraints);
    }

    public void addPreviousListener(ActionListener listener) {
        playbackPanel.addPreviousListener(listener);
    }

    public void addStopListener(ActionListener listener) {
        playbackPanel.addStopListener(listener);
    }

    public void addPlayListener(ActionListener listener) {
        playbackPanel.addPlayListener(listener);
    }

    public void addNextListener(ActionListener listener) {
        playbackPanel.addNextListener(listener);
    }

    public void addPlaybackSettingsMouseListener(MouseListener listener) {
        playbackSettingsPanel.addMouseListener(listener);
    }

    public void setPlayIcon(Icon icon) {
        playbackPanel.setPlayIcon(icon);
    }

    public void setArtist(String artist) {
        songPanel.setArtistValue(artist);
    }

    public void setTitle(String title) {
        songPanel.setTitleValue(title);
    }




}
