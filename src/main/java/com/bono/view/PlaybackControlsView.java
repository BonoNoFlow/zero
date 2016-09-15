package com.bono.view;

import com.bono.controls.Volume;
import com.bono.controls.VolumeButton;
import com.bono.icon.OptionIcon;
import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;

/**
 * Created by bono on 8/11/16.
 */
public class PlaybackControlsView extends JPanel implements PlaybackView, PlaybackScroller {

    public static final String PREVIOUS_BUTTON = "previous_button";
    public static final String STOP_BUTTON = "stop_button";
    public static final String PLAY_BUTTON = "play_button";
    public static final String NEXT_BUTTON = "next_button";
    public static final String OPTIONS_BUTTON = "options_button";
    public static final String VOLUME_BUTTON = "volume_button";

    private HashMap<String, Button> buttons = new HashMap<>();

    private JLabel artist;
    private JLabel title;
    private JTextField artistValue;
    private JTextField titleValue;
    private JTextField playingTime;
    private JTextField totalTime;
    private JSlider playtime;

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
        BonoIcon stop = BonoIconFactory.getStopButtonIcon();
        stop.setUnpressedColor(new Color(240, 97, 81));
        stop.setPressedColor(new Color(201, 78, 64));
        panel.add(build(STOP_BUTTON, stop, null));
        panel.add(build(PLAY_BUTTON, BonoIconFactory.getPlayButtonIcon(), null));
        panel.add(build(NEXT_BUTTON, BonoIconFactory.getNextButtonIcon(), null));
        return panel;
    }

    private ControlButton build(String actionCommand, Icon icon, String text) {
        ControlButton button = new ControlButton(actionCommand);
        if (icon != null && icon instanceof BonoIcon) {
            BonoIcon bonoIcon = (BonoIcon) icon;
            bonoIcon.setIconHeight(14);
            bonoIcon.setIconWidth(14);
            button.setButtonIcon(bonoIcon);
        } else {
            button.setButtonIcon(icon);
        }
        button.setButtonText(text);
        button.setMargin(new Insets(4,4,4,4));
        buttons.put(actionCommand, button);
        return button;
    }

    private JTextField buildJTextField(int columns, boolean editable, Border border, int alignment, Font font) {
        JTextField field = new JTextField(columns);
        field.setEditable(editable);
        field.setBorder(border);
        field.setHorizontalAlignment(alignment);
        field.setFont(font);
        field.setBackground(UIManager.getColor("Panel.background"));
        return field;
    }

    private JPanel songPanel() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel(new GridBagLayout());

        artist = new JLabel("artist: ");
        title = new JLabel("title: ");

        artistValue = buildJTextField(15, false, null, JTextField.LEFT, null);

        titleValue = buildJTextField(15, false, null, JTextField.LEFT, null);

        GridBagConstraints constraints = new GridBagConstraints();

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

        boxPanel.add(panel);
        JPanel sliderPanel = new JPanel();

        playingTime = buildJTextField(8, false, null, JTextField.RIGHT, new Font("Times Roman", Font.PLAIN, 10));
        sliderPanel.add(playingTime);

        playtime = new JSlider();
        sliderPanel.add(playtime);

        totalTime = buildJTextField(8, false, null, JTextField.LEFT, new Font("Times Roman", Font.PLAIN, 10));
        sliderPanel.add(totalTime);

        boxPanel.add(sliderPanel);

        return boxPanel;
    }

    private JPanel optionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        volume = new VolumeButton();
        panel.add(volume);
        ControlButton options = build(OPTIONS_BUTTON, new OptionIcon(), null);
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
        artistValue.setToolTipText(artist);
        artistValue.setCaretPosition(0);
        titleValue.setText(title);
        titleValue.setToolTipText(title);
        titleValue.setCaretPosition(0);
    }

    @Override
    public void setMinimum(int minimum) {
        playtime.setMinimum(minimum);
    }

    @Override
    public void setMaximum(int maximum) {
        playtime.setMaximum(maximum);
    }

    @Override
    public int getMaximum() {
        return playtime.getMaximum();
    }

    @Override
    public void setValue(int value) {
        playtime.setValue(value);
    }

    @Override
    public int getValue() {
        return playtime.getValue();
    }

    @Override
    public void setTotalTime(String totalTime) {
        this.totalTime.setText(totalTime);
    }

    @Override
    public void setPlayingTime(String playingTime) {
        this.playingTime.setText(playingTime);
    }

    @Override
    public void addScrollerMouseListener(MouseListener l) {
        playtime.addMouseListener(l);
    }

    @Override
    public void addScrollerChangeListener(ChangeListener l) {
        playtime.addChangeListener(l);
    }

    @Override
    public void removeScrollerChangeListener(ChangeListener l) {
        playtime.removeChangeListener(l);
    }
}
