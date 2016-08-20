package com.bono.view;

import com.bono.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationView  {

    private JFrame frame;
    private JSplitPane splitPane;
    private PlaybackControlsView playbackControlsView;
    private CurrentPlaylistView currentPlaylistView;
    private SoundcloudView soundcloudView;
    private DirectoryView directoryView;
    private DatabaseBrowserView databaseBrowserView;

    private VersionPanel versionPanel;

    private JMenuBar menubar = new JMenuBar();
    private JMenu menu = new JMenu("File");
    private JMenuItem configItem = new JMenuItem("config");

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

        playbackControlsView = new PlaybackControlsView();
        frame.getContentPane().add(playbackControlsView, BorderLayout.NORTH);

        soundcloudView = new SoundcloudView();

        //directoryView = new DirectoryView();
        databaseBrowserView = new DatabaseBrowserView();

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setTabPlacement(SwingConstants.TOP);
        //tabbedPane.setUI(new CustemTabbedPaneUI());
        //tabbedPane.addTab("database", directoryView.getScrollPane());
        tabbedPane.addTab("database", databaseBrowserView);
        tabbedPane.addTab("soundcloud", soundcloudView);



        currentPlaylistView = new CurrentPlaylistView();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setLeftComponent(tabbedPane);
        //splitPane.setRightComponent(playlistView);
        splitPane.setRightComponent(currentPlaylistView);
        splitPane.setDividerLocation(0.5);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        versionPanel = new VersionPanel();

        frame.getContentPane().add(versionPanel, BorderLayout.SOUTH);

        menu.add(configItem);
        menubar.add(menu);
        frame.setJMenuBar(menubar);
    }

    public void addConfigmenuItemlistener(ActionListener l) {
        configItem.addActionListener(l);
    }

    public PlaybackView getPlaybackControlsView() {
        return playbackControlsView;
    }




    public PlaylistView getCurrentPlaylistView() {
        return currentPlaylistView;
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }

    public DirectoryView getDirectoryView() {
        return directoryView;
    }

    public BrowserView getDatabaseBrowserView() {
        return databaseBrowserView;
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
