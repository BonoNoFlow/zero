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
import javax.swing.tree.DefaultMutableTreeNode;
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

    private JList list;

    private DBExecutor dbExecutor;
    private MPDPlaylist playlist = new MPDPlaylist();

    // used by TestList.
    public PlaylistController(DBExecutor dbExecutor, JList list) {
        this.dbExecutor = dbExecutor;
        this.list = list;
        this.list.addMouseListener(this);
        this.playlist.addListener(new AdditionalTrackInfo(SoundcloudController.CLIENTID));
        init();
    }

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
        //playlistView.setModel(playlist.getModel());
    }

    private void updateModel() {
        //if ()
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
                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("play", new PlayListener());
                popup.addMenuItem("remove", new RemoveListener());
                popup.show(list, e.getX(), e.getY());
            }
        }
    }

    private class PlayListener implements ActionListener {



        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = list.getSelectionModel();
            int track = model.getAnchorSelectionIndex();

            Song song = playlist.getSong(track);
            //Song song = playlist.getVectorSong(track);

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



        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = list.getSelectionModel();
            int track = model.getAnchorSelectionIndex();

            Song song = playlist.getSong(track);
            //Song song = playlist.getVectorSong(track);
            System.out.println(song.toString());
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
            } else {
                //DefaultMutableTreeNode node = (DefaultMutableTreeNode) dtde.;
                System.out.println(d);
            }
        }
    }
}
