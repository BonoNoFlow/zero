package com.bono.database;

import com.bono.api.ChangeListener;
import com.bono.api.ClientExecutor;
import com.bono.api.MPDClient;
import com.bono.api.Status;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.BrowserView;
import com.bono.view.SoundcloudView;
import com.bono.view.StoredPlaylistsView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

/**
 * Created by bono on 8/20/16.
 */
public class MusicDatabase implements javax.swing.event.ChangeListener {

    private DatabaseBrowser databaseBrowser;

    private SoundcloudController soundcloudController;

    private StoredPlaylistsController storedPlaylistsController;

    private ClientExecutor clientExecutor;

    private MPDClient mpdClient;

    private Status status;

    public MusicDatabase(MPDClient mpdClient) {
        this.mpdClient = mpdClient;
        databaseBrowser = new DatabaseBrowser(mpdClient);
        soundcloudController = new SoundcloudController(mpdClient);
        storedPlaylistsController = new StoredPlaylistsController(mpdClient);
    }



    public void initDatabaseBrowserView(BrowserView view) {
        databaseBrowser.initBrowserView(view);
    }

    public void setSoundcloudView(SoundcloudView view) {
        soundcloudController.setSoundcloudView(view);
    }

    public void setStoredPlaylistsController(StoredPlaylistsView view) {
        storedPlaylistsController.setStoredPlaylistsView(view);
    }
    /*
    Listener listens to the selected tab changes.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
            int selected = tabbedPane.getSelectedIndex();
            switch (selected) {
                case 0:
                case 1:
                case 2:
                    storedPlaylistsController.init();
                    break;
            }
        }
    }

    public StoredPlaylistsController getStoredPlaylistsController() {
        return storedPlaylistsController;
    }
}
