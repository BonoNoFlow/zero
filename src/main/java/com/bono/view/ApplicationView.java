package com.bono.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationView  {

    private JFrame frame;
    private SoundcloudView soundcloudView;

    public ApplicationView() {
        build();
    }

    private void build() {
        frame = new JFrame("zero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        soundcloudView = new SoundcloudView();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);
        tabbedPane.addTab("soundcloud", soundcloudView);

        JScrollPane pane = new JScrollPane();
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        pane.getViewport().add(tabbedPane);
        frame.getContentPane().add(pane, BorderLayout.CENTER);
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }

    public void view() {
        frame.pack();
        frame.setVisible(true);
    }
}
