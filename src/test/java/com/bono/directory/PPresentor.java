package com.bono.directory;

import com.bono.Utils;
import com.bono.api.*;
import com.bono.controls.Player;
import com.bono.playlist.PlaylistPresenter;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class PPresentor {

    private PlaylistView playlistView;

    private Playlist playlist;

    private Player player;

    private DefaultListModel<Song> songs;

    private DBExecutor dbExecutor;

    // listeners of this class.
    private PPresentor.DroppedListener droppedListener;
    private PPresentor.IdleListener idleListener;

    public Song song() {
        return new Song();
    }

    public DropTargetListener getDroppedListener() {
        if (droppedListener == null) {
            droppedListener = new PPresentor.DroppedListener();
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
            idleListener = new PPresentor.IdleListener();
        }
        return idleListener;
    }

    private class IdleListener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            String event = (String) eventObject.getSource();
            if (event.equals("playlist")) {
                //initPlaylist();
            }
        }
    }
}
