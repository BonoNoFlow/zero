package com.bono.database;

import com.bono.api.ChangeListener;
import com.bono.api.ClientExecutor;
import com.bono.api.MPDClient;
import com.bono.api.Status;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.BrowserView;
import com.bono.view.SoundcloudView;

/**
 * Created by bono on 8/20/16.
 */
public class MusicDatabase {

    private DatabaseBrowser databaseBrowser;

    private SoundcloudController soundcloudController;

    private ClientExecutor clientExecutor;

    private MPDClient mpdClient;

    private Status status;

    public MusicDatabase(MPDClient mpdClient) {
        this.mpdClient = mpdClient;
        databaseBrowser = new DatabaseBrowser(mpdClient);
        soundcloudController = new SoundcloudController(mpdClient);
    }



    public void initDatabaseBrowserView(BrowserView view) {
        databaseBrowser.initBrowserView(view);
    }

    public void setSoundcloudView(SoundcloudView view) {
        soundcloudController.setSoundcloudView(view);
    }


}
