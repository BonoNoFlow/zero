package com.bono.view;

import com.bono.Utils;

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

        frame.getContentPane().setPreferredSize(Utils.screenSize());


        soundcloudView = new SoundcloudView();

        DirectoryView directoryView = new DirectoryView();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);
        tabbedPane.addTab("soundcloud", soundcloudView);
        tabbedPane.addTab("directory", directoryView);

        playlistView = new PlaylistView();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(tabbedPane);
        splitPane.setRightComponent(playlistView);

        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    public PlaylistView getPlaylistView() {
        return playlistView;
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }

    public void view() {
        frame.pack();
        frame.setVisible(true);
    }
}
