package com.bono.view;

import com.bono.ApplicationMain;
import com.bono.laf.BonoSplitPaneUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

/**
 * Created by hendriknieuwenhuis on 23/02/16.
 */
public class ApplicationView  {

    private JFrame frame = new JFrame("zero");
    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private PlaybackControlsView playbackControlsView = new PlaybackControlsView();
    private CurrentPlaylistView currentPlaylistView = new CurrentPlaylistView();
    private SoundcloudView soundcloudView = new SoundcloudView();
    private DatabaseBrowserView databaseBrowserView = new DatabaseBrowserView();
    private StoredPlaylistsView storedPlaylistsView = new StoredPlaylistsView();
    private VersionPanel versionPanel = new VersionPanel();
    private JMenuBar menubar = new JMenuBar();
    private JMenu menu = new JMenu("File");
    private JMenuItem configItem = new JMenuItem("config");
    private JMenuItem savePlaylist = new JMenuItem("save playlist");

    public ApplicationView(Dimension dimension, WindowAdapter adapter) {
        build(dimension, adapter);
    }

    private void build(Dimension dimension, WindowAdapter adapter) {
        frame.addWindowListener(adapter);
        frame.getContentPane().setPreferredSize(dimension);
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(2,4,2,3));

        Dimension screen = ApplicationMain.screenDimension();

        frame.setLocation(((screen.width / 2) - (dimension.width / 2)), ((screen.height / 2) - (dimension.height / 2)));
        frame.getContentPane().add(playbackControlsView, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.TOP);
        tabbedPane.addTab("database", databaseBrowserView);
        tabbedPane.addTab("soundcloud", soundcloudView);
        tabbedPane.addTab("playlists", storedPlaylistsView);

        splitPane.setUI(new BonoSplitPaneUI());
        splitPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        splitPane.setDividerSize(5);
        splitPane.setContinuousLayout(true);
        splitPane.setLeftComponent(tabbedPane);
        splitPane.setRightComponent(currentPlaylistView);

        // increase divider width or height
        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
        Rectangle bounds = divider.getBounds();
        if( splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ) {
            bounds.x -= 4;
            bounds.width = 9;
        } else {
            bounds.y -= 4;
            bounds.height = 9;
        }
        divider.setBounds( bounds );

        splitPane.setDividerLocation(0.5);

        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
        frame.getContentPane().add(versionPanel, BorderLayout.SOUTH);

        menu.add(configItem);
        menu.add(savePlaylist);
        menubar.add(menu);
        frame.setJMenuBar(menubar);
    }

    public void addConfigMenuItemlistener(ActionListener l) {
        configItem.addActionListener(l);
    }

    public void addSavePlaylistMenuItemListener(ActionListener l) {
        savePlaylist.addActionListener(l);
    }

    public PlaybackControlsView getPlaybackControlsView() {
        return playbackControlsView;
    }

    public PlaylistView getCurrentPlaylistView() {
        return currentPlaylistView;
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
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
