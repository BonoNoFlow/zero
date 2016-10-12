package com.bono.view;

import com.bono.Application;
import com.bono.laf.BonoSplitPaneDivider;
import com.bono.laf.BonoSplitPaneUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
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
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(2,4,2,3));

        Dimension screen = Application.screenDimension();
        frame.setLocation(((screen.width / 2) - (dimension.width / 2)), ((screen.height / 2) - (dimension.height / 2)));

        playbackControlsView = new PlaybackControlsView();
        frame.getContentPane().add(playbackControlsView, BorderLayout.NORTH);

        soundcloudView = new SoundcloudView();

        databaseBrowserView = new DatabaseBrowserView();

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setTabPlacement(SwingConstants.TOP);
        //tabbedPane.setUI(new CustemTabbedPaneUI());
        //tabbedPane.addTab("database", directoryView.getScrollPane());

        tabbedPane.addTab("database", databaseBrowserView);
        tabbedPane.addTab("soundcloud", soundcloudView);




        currentPlaylistView = new CurrentPlaylistView();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
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

        //SplitPaneUI splitPaneUI = splitPane.getUI();
        //splitPaneUI.installUI(new BonoSplitPaneDivider((BasicSplitPaneUI)splitPaneUI));

        //BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
        //divider.
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
