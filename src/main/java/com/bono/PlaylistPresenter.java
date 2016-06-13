package com.bono;

import com.bono.api.*;
import com.bono.controls.*;
import com.bono.controls.CurrentPlaylist;
import com.bono.view.MPDPopup;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class PlaylistPresenter extends MouseAdapter {

    private PlaylistView playlistView;

    private Playlist playlist;

    private Player player;

    private DefaultListModel<Song> songs;

    private DBExecutor dbExecutor;

    // listeners of this class.
    private PlaylistPresenter.DroppedListener droppedListener;
    private PlaylistPresenter.IdleListener idleListener;

    public PlaylistPresenter(DBExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
        playlist = new Playlist();
        playlist.addListener(new PlaylistChangeListener());

    }

    public PlaylistPresenter(DBExecutor dbExecutor, Player player) {
        this(dbExecutor);
        this.player = player;
    }
    /*
    Adds the view to the presenter.

    Also adds a droptargetadapter to the view.

     */
    public void addView(PlaylistView view) {
        playlistView = view;
        playlistView.addDropTargetListener(getDroppedListener());
    }

    public void initPlaylist() {
        String value = "";

        try {
            value = dbExecutor.execute(new DefaultCommand(Playlist.PLAYLISTINFO));
            playlist.clear();
            playlist.populate(value);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("Clicked");

            if (!playlistView.getPlaylist().getSelectionModel().isSelectionEmpty()) {
                MPDPopup popup = new MPDPopup();
                popup.addMenuItem("play", new PlaylistPresenter.PlayListener());
                popup.addMenuItem("remove", new PlaylistPresenter.RemoveListener());
                popup.show(playlistView.getPlaylist(), e.getX(), e.getY());
            }
        }
    }
    private class PlayListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = playlistView.getPlaylist().getSelectionModel();
            int track = model.getAnchorSelectionIndex();
            Song song = playlist.getSong(track);
            try {
                player.getPlayerControl().playid(song.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class RemoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = playlistView.getPlaylist().getSelectionModel();
            int track = model.getAnchorSelectionIndex();
            Song song = playlist.getSong(track);

            try {
                dbExecutor.execute(new DefaultCommand(Playlist.DELETE_ID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private class PlaylistChangeListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            Playlist playlist = (Playlist) eventObject.getSource();
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
    }




    public Song song(String id) {
        Song song = null;
        try {
            song =  new Song(dbExecutor.execute(new DefaultCommand(Playlist.PLAYLISTID, id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return song;
    }

    public DropTargetListener getDroppedListener() {
        if (droppedListener == null) {
            droppedListener = new PlaylistPresenter.DroppedListener();
        }
        return droppedListener;
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

            String reply = "";
            if (d.startsWith("http") || d.startsWith("https")) {
                try {
                    reply = dbExecutor.execute(new DefaultCommand(Playlist.LOAD, Utils.loadUrl(d)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {

                Utils.Log.print(d);
            }
        }
    }

    public ChangeListener getIdleListener() {
        if (idleListener == null) {
            idleListener = new PlaylistPresenter.IdleListener();
        }
        return idleListener;
    }

    private class IdleListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            String event = (String) eventObject.getSource();
            if (event.equals("playlist")) {
                initPlaylist();
            }
        }
    }
}
