package com.bono.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationView  {

    private JFrame frame;
    private PlaylistView playlistView;
    private SoundcloudView soundcloudView;

    public ApplicationView() {
        build();
    }

    private void build() {
        frame = new JFrame("zero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane(SwingConstants.HORIZONTAL);

        soundcloudView = new SoundcloudView();





        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);
        tabbedPane.addTab("soundcloud", soundcloudView);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }

    public void view() {
        frame.pack();
        frame.setVisible(true);
    }
}
