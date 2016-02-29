package com.bono.view.company;

import com.bono.icons.BonoIconFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * bbbb
 * Created by hendriknieuwenhuis on 26/11/15.
 */
public class PlaybackView extends JPanel {

    private JButton previous;
    private JButton play;
    private JButton next;

    public PlaybackView() {
        super();
        build();
    }

    private void build() {
        previous = new JButton(BonoIconFactory.getPreviousButtonIcon());
        previous.setActionCommand("previous");
        play = new JButton(BonoIconFactory.getPlayButtonIcon());
        play.setActionCommand("play");
        next = new JButton(BonoIconFactory.getNextButtonIcon());
        next.setActionCommand("next");

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(previous);
        add(play);
        add(next);

        //setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void addPreviousListener(ActionListener listener) {
        previous.addActionListener(listener);
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
