package com.bono.playlist;

import com.bono.Utils;
import com.bono.api.Playlist;
import com.bono.api.Song;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.properties.PlayerProperties;
import com.bono.soundcloud.AdditionalTrackInfo;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.PlaylistView;
import com.bono.view.MPDPopup;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class PlaylistController extends MouseAdapter {

    private PlaylistView playlistView;
    private DBExecutor dbExecutor;
    private MPDPlaylist playlist = new MPDPlaylist();

    public PlaylistController(DBExecutor dbExecutor, PlaylistView playlistView) {
        this.dbExecutor = dbExecutor;
        this.playlistView = playlistView;
        this.playlistView.addMouseListener(this);
        this.playlistView.addDropTargetListener(new DropedListener());
        this.playlist.addListener(new AdditionalTrackInfo(SoundcloudController.CLIENTID));
        init();
    }

    private void init() {
        String entry = "";
        try {
            entry = dbExecutor.execute(new MPDCommand(PlaylistProperties.PLAYLISTINFO));
        } catch (Exception e) {
            e.printStackTrace();
        }
        playlist.populate(entry);
        playlistView.setModel(playlist.getModel());
    }

    public void update() {
        init();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (e.getButton() == MouseEvent.BUTTON3) {

            ListSelectionModel model = ((JList) e.getSource()).getSelectionModel();

            if (!model.isSelectionEmpty()) {

                PlaylistPopup playlistPopup = new PlaylistPopup();

                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("play", new PlayListener(model));
                popup.addMenuItem("remove", new RemoveListener(model));
                popup.show(playlistView.getPlaylist(), e.getX(), e.getY());
            }
        }
    }

    private class PlayListener implements ActionListener {

        private ListSelectionModel model;

        public PlayListener(ListSelectionModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int track = model.getAnchorSelectionIndex();

            Song song = playlist.getSong(track);

            System.out.println(song.getId());

            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAYID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class RemoveListener implements ActionListener {

        private ListSelectionModel model;

        public RemoveListener(ListSelectionModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int track = model.getAnchorSelectionIndex();

            Song song = playlist.getSong(track);

            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand(PlaylistProperties.DELETE_ID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class DropedListener extends DropTargetAdapter {

        @Override
        public void drop(DropTargetDropEvent dtde) {
            String d = "";
            try {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                d = (String) dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
                System.out.println(d);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String reply = "";
            if (d.startsWith("http") || d.startsWith("https")) {
                try {
                    reply = dbExecutor.execute(new MPDCommand("load", Utils.loadUrl(d)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
