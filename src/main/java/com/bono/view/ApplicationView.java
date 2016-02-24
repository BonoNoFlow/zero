package com.bono.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationView  {

    private JFrame frame;
    private SoundcloudPanel soundcloudPanel;

    public ApplicationView() {
        build();
    }

    private void build() {
        frame = new JFrame("zero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        soundcloudPanel = new SoundcloudPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.LEFT);
        tabbedPane.addTab("soundcloud", soundcloudPanel);

        JScrollPane pane = new JScrollPane();
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        pane.getViewport().add(tabbedPane);
        frame.getContentPane().add(pane, BorderLayout.CENTER);
    }

    public SoundcloudPanel getSoundcloudPanel() {
        return soundcloudPanel;
    }

    public void view() {
        frame.pack();
        frame.setVisible(true);
    }
}
