package com.bono.view;

import com.bono.controls.SongPlaybackScroller;
import com.bono.controls.Volume;
import com.bono.controls.VolumeButton;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by bono on 8/11/16.
 */
public class PlaybackControlsView extends JPanel implements PlaybackView {

    public static final String PREVIOUS_BUTTON = "previous_button";
    public static final String STOP_BUTTON = "stop_button";
    public static final String PLAY_BUTTON = "play_button";
    public static final String NEXT_BUTTON = "next_button";
    public static final String OPTIONS_BUTTON = "options_button";
    public static final String VOLUME_BUTTON = "volume_button";

    private HashMap<String, Button> buttons = new HashMap<>();

    private JLabel artist;
    private JLabel title;
    private JLabel artistValue;
    private JLabel titleValue;
    private JSlider playtime;

    private SongPlaybackScroller songPlaybackScroller;

    private VolumeButton volume;

    public PlaybackControlsView() {
        super();
        build();
    }

    private void build() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        constraints.weightx = 1.0;

        constraints.anchor = GridBagConstraints.LINE_START;
        add(playerControls(), constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        add(songPanel(), constraints);

        constraints.anchor = GridBagConstraints.LINE_END;
        add(optionsPanel(), constraints);
    }

    private JPanel playerControls() {
        JPanel panel = new JPanel();
        panel.add(build(PREVIOUS_BUTTON, BonoIconFactory.getPreviousButtonIcon(), null));
        panel.add(build(STOP_BUTTON, BonoIconFactory.getStopButtonIcon(), null));
        panel.add(build(PLAY_BUTTON, BonoIconFactory.getPlayButtonIcon(), null));
        panel.add(build(NEXT_BUTTON, BonoIconFactory.getNextButtonIcon(), null));
        return panel;
    }

    private ControlButton build(String actionCommand, Icon icon, String text) {
        ControlButton button = new ControlButton(actionCommand);
        if (icon != null) {
            BonoIcon bonoIcon = (BonoIcon) icon;
            bonoIcon.setIconHeight(14);
            bonoIcon.setIconWidth(14);
            button.setButtonIcon(bonoIcon);
        }
        button.setButtonText(text);
        button.setMargin(new Insets(4,4,4,4));
        buttons.put(actionCommand, button);
        return button;
    }

    private JPanel songPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        artist = new JLabel("artist: ");
        title = new JLabel("title: ");
        artistValue = new JLabel();
        titleValue = new JLabel();

        //GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        //panel.setLayout(layout);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        panel.add(artist, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(artistValue, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        panel.add(title, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(titleValue, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        playtime = new JSlider();
        panel.add(playtime, constraints);

        return panel;
    }

    private JPanel optionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //JMenuBar bar = new JMenuBar();
        //JMenu menu = new JMenu("vol");
        //JSlider vol = new JSlider(JSlider.VERTICAL);
        //menu.add(vol);
        //bar.add(menu);
        volume = new VolumeButton();
        panel.add(volume);
        ControlButton options = build(OPTIONS_BUTTON, null, "options");
        panel.add(options);
        return panel;
    }

    @Override
    public HashMap<String, Button> getButtons() {
        return buttons;
    }

    @Override
    public Volume getVolume() {
        return volume;
    }

    @Override
    public void setPlayingSong(String artist, String title) {
        artistValue.setText(artist);
        titleValue.setText(title);
    }
}