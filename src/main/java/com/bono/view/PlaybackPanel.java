package com.bono.view;

import com.bono.icons.BonoIconFactory;
import com.bono.api.PlayerProperties;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * bbbb
 * Created by hendriknieuwenhuis on 26/11/15.
 */
public class PlaybackPanel extends JPanel {

    private JButton previous;
    private JButton play;
    private JButton next;

    public PlaybackPanel() {
        super();
        build();
    }

    private void build() {
        previous = new JButton(BonoIconFactory.getPreviousButtonIcon());
        previous.setActionCommand(PlayerProperties.PREVIOUS);
        play = new JButton(BonoIconFactory.getPlayButtonIcon());
        play.setActionCommand(PlayerProperties.PAUSE);
        next = new JButton(BonoIconFactory.getNextButtonIcon());
        next.setActionCommand(PlayerProperties.NEXT);

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
