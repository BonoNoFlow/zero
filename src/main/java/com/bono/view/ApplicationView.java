package com.bono.view;

import com.bono.Application;
import com.bono.CustemTabbedPaneUI;
import com.bono.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationView  {

    private JFrame frame;
    private JSplitPane splitPane;
    private PlaybackView playbackView;
    private PlaylistView playlistView;
    private SoundcloudView soundcloudView;
    private DirectoryView directoryView;

    private VersionPanel versionPanel;

    public ApplicationView(Dimension dimension, WindowAdapter adapter) {
        build(dimension, adapter);
    }

    private void build(Dimension dimension, WindowAdapter adapter) {
        frame = new JFrame("zero");

        // set default closing operation
        // to exit on close when,
        // WindowListener is not present!
        if (adapter != null) {
            frame.addWindowListener(adapter);
        } else {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        frame.getContentPane().setPreferredSize(dimension);
        Dimension screen = Application.screenDimension();
        frame.setLocation(((screen.width / 2) - (dimension.width / 2)), ((screen.height / 2) - (dimension.height / 2)));

        playbackView = new PlaybackView();
        frame.getContentPane().add(playbackView, BorderLayout.NORTH);

        soundcloudView = new SoundcloudView();

        directoryView = new DirectoryView();

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setTabPlacement(SwingConstants.TOP);
        //tabbedPane.setUI(new CustemTabbedPaneUI());
        tabbedPane.addTab("database", directoryView.getScrollPane());
        tabbedPane.addTab("soundcloud", soundcloudView);


        playlistView = new PlaylistView();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setLeftComponent(tabbedPane);
        splitPane.setRightComponent(playlistView);
        splitPane.setDividerLocation(0.5);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        versionPanel = new VersionPanel();

        frame.getContentPane().add(versionPanel, BorderLayout.SOUTH);
    }

    public Playback getPlaybackView() {
        return playbackView;
    }


    public PlaylistView getPlaylistView() {
        return playlistView;
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }

    public DirectoryView getDirectoryView() {
        return directoryView;
    }

    public VersionPanel getVersionPanel() {
        return versionPanel;
    }

    public void show() {

        frame.pack();
        splitPane.setDividerLocation(0.3);
        frame.setVisible(true);
    }

    public boolean isIconified() {
        return frame.isBackgroundSet();
    }
}
