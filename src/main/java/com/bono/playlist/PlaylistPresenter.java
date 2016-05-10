package com.bono.playlist;

import com.bono.api.DBExecutor;
import com.bono.api.PlayerControl;
import com.bono.api.PlaylistControl;
import com.bono.view.PlaylistView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 06/05/16.
 */
public class PlaylistPresenter implements ActionListener {

    private PlaylistView playlistView;
    private PlaylistModel playlistModel;
    private PlaylistControl playlistControl;

    private DBExecutor dbExecutor;

    public PlaylistPresenter(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    private void init() {
        playlistModel = new PlaylistModel();
        playlistControl = new PlaylistControl(dbExecutor);
    }

    private void addView(PlaylistView view) {
        playlistView = view;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
