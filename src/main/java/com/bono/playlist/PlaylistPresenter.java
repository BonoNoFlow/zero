package com.bono.playlist;

import com.bono.Utils;
import com.bono.api.*;
import com.bono.controls.Playback;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 06/05/16.
 */
public class PlaylistPresenter implements ChangeListener {

    private PlaylistView playlistView;

    private PlaylistControl playlistControl;

    private Playlist playlist;

    private Playback playback;

    private DefaultListModel<Song> songs;

    private DBExecutor dbExecutor;

    public PlaylistPresenter(DBExecutor dbExecutor, Playback playback) {
        this.playback = playback;
        playlistControl = new PlaylistControl(dbExecutor);
        playlist = new Playlist();
    }

    private void init() {

    }

    /*
    Adds the view to the presenter.

    Also adds a droptargetadapter to the view.

     */
    public void addView(PlaylistView view) {
        playlistView = view;
        playlistView.addDropTargetListener(new DroppedListener());
    }

    public void initPlaylist() {
        String value = "";

        try {
            value = playlistControl.playlistinfo(null);
            playlist.clear();
            playlist.populate(value);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        songs = new DefaultListModel<>();
        Iterator<Song> i = playlist.iterator();
        while (i.hasNext()) {
            songs.addElement(i.next());
        }
        if (playlistView != null) {
            SwingUtilities.invokeLater(() -> {
                playlistView.getPlaylist().setModel(songs);
            });
        }
    }

    @Override
    public void stateChanged(EventObject eventObject) {

    }

    /*

        DropTargetListener

        The drop target accepts http / https urls, or files
        from the directory view.
         */
    private class DroppedListener extends DropTargetAdapter {

        @Override
        public void drop(DropTargetDropEvent dtde) {

            String d = "";
            try {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                d = (String) dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(d);
            /*
            String reply = "";
            if (d.startsWith("http") || d.startsWith("https")) {
                try {
                    reply = dbExecutor.execute(new DefaultCommand("load", Utils.loadUrl(d)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {

                Utils.Log.print(d);
            }*/
        }
    }
}
