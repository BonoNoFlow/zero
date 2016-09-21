package com.bono.playlist;

import com.bono.api.*;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.PlaylistView;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class  PlaylistPresenter extends MouseAdapter {

    private PlaylistView playlistView;

    private Playlist playlist;

    private PlaylistModel playlistModel;

    private MPDClient mpdClient;

    // listeners of this class.
    private PlaylistPresenter.DroppedListener droppedListener;
    private PlaylistPresenter.IdleListener idleListener;

    public PlaylistPresenter(MPDClient client) {
        this.mpdClient = client;
        playlist = client.getPlaylist();
        playlistModel = new PlaylistModel(playlist);
        mpdClient.getStatus().addListener(new StatusListener());
    }

    /*
    Adds the view to the presenter.
    Also adds a droptargetadapter to the view.
     */
    public void addView(PlaylistView playlistView) {
        this.playlistView = playlistView;
        this.playlistView.setModel(playlistModel);
        this.playlistView.addDropTargetListener(getDroppedListener());
    }

    public void addPlaylistListener(ChangeListener changeListener) {
        playlist.addListener(changeListener);
    }

    public void initPlaylist() {
        try {
            playlist.queryPlaylist();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        playlistModel.setPlaylist(playlist);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // play the double clicked song.
        if (e.getClickCount() == 2) {
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {

                int[] rows = playlistView.getSelectedRows();
                if (rows.length == 1) {
                    Song song = playlist.getSong(rows[0]);
                    try {
                        mpdClient.getPlayer().playId(song.getId());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    playlistView.getSelectionModel().clearSelection();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        showPopup(e);
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            playlistView.getSelectionModel().setValueIsAdjusting(false);
            if (!playlistView.getSelectionModel().isSelectionEmpty()) {
                PlaylistPopup p = new PlaylistPopup(mpdClient, playlistView, playlistModel);
                p.show(e.getX(), e.getY());
            }
        }
    }

    public Playlist getPlaylist() {
        return playlist;
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
        from the database view.
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
            //System.out.println(d);


            if (d.startsWith("http") || d.startsWith("https")) {
                try {

                    playlist.load(SoundcloudController.loadUrl(d));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // TODO popup?
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
            if (eventObject.getSource() instanceof String) {
                String event = (String) eventObject.getSource();
                if (event.equals("playlist")) {
                    initPlaylist();
                }
            }
        }
    }

    private class StatusListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            if (eventObject.getSource() instanceof Status) {
                Status status = (Status) eventObject.getSource();

                if (status.getState() != Status.STOP_STATE) {
                    playlistView.getPlayingRenderer().setPlaying(status.getSong());

                } else {
                    playlistView.getPlayingRenderer().setPlaying(-1);
                }

                // redraw JList with the currently playing song highlighted.
                playlistView.redraw();

            }
        }
    }
}
