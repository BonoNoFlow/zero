package com.bono.view;

import com.bono.icons.BonoIcon;
import com.bono.icons.BonoIconFactory;
import com.bono.api.PlayerProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * bbbb
 * Created by hendriknieuwenhuis on 26/11/15.
 */
public class PlaybackPanel extends JPanel {

    public static final int ICON_HEIGHT = 14;
    public static final int ICON_WIDTH = 14;
    public static final Insets INSETS = new Insets(4,4,4,4);

    private JButton previous;
    private JButton stop;
    private JButton play;
    private JButton next;

    public PlaybackPanel() {
        super();
        build();
    }

    private void build() {
        previous = buildButton(BonoIconFactory.getPreviousButtonIcon(), PlayerProperties.PREVIOUS);
        stop = buildButton(BonoIconFactory.getStopButtonIcon(), PlayerProperties.STOP);
        play = buildButton(BonoIconFactory.getPlayButtonIcon(), PlayerProperties.PAUSE);
        next = buildButton(BonoIconFactory.getNextButtonIcon(), PlayerProperties.NEXT);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(previous);
        add(stop);
        add(play);
        add(next);

        //setBorder(BorderFactory.createLineBorder(Color.black));
    }

    private JButton buildButton(Icon icon, String actionCommand) {
        BonoIcon bonoIcon = (BonoIcon) icon;
        bonoIcon.setIconHeight(PlaybackPanel.ICON_HEIGHT);
        bonoIcon.setIconWidth(PlaybackPanel.ICON_WIDTH);
        JButton button = new JButton(bonoIcon);
        button.setActionCommand(actionCommand);
        button.setMargin(PlaybackPanel.INSETS);
        return button;
    }

    public void addPreviousListener(ActionListener listener) {
        previous.addActionListener(listener);
    }

    public void addStopListener(ActionListener listener) {
        stop.addActionListener(listener);
    }

    public void addPlayListener(ActionListener listener) {
        play.addActionListener(listener);
    }

    public void addNextListener(ActionListener listener) {
        next.addActionListener(listener);
    }

    public void setPlayIcon(Icon icon) {
        play.setIcon(icon);
    }
}
