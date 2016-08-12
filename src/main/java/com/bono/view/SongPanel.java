package com.bono.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 26/11/15.
 */
@Deprecated
public class SongPanel extends JPanel {

    private JLabel artist;
    private JLabel title;
    private JLabel artistValue;
    private JLabel titleValue;

    public SongPanel() {
        super();
        build();
    }

    private void build() {
        artist = new JLabel("artist: ");
        title = new JLabel("title: ");
        artistValue = new JLabel();

        titleValue = new JLabel();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(artist, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add(artistValue, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(title, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        add(titleValue, constraints);
    }

    public void setArtistValue(String value) {
        artistValue.setText(value);
    }

    public void setTitleValue(String value) {
        titleValue.setText(value);
    }
}
