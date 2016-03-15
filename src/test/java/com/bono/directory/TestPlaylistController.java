package com.bono.directory;

import com.bono.Utils;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.Playlist;
import com.bono.api.Song;
import com.bono.playlist.PlaylistProperties;
import com.bono.properties.PlayerProperties;
import com.bono.view.MPDPopup;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 15/03/16.
 */
public class TestPlaylistController extends MouseAdapter implements ChangeListener {

    private DefaultListModel<Song> songs = new DefaultListModel<>();

    private JList list;

    private DBExecutor dbExecutor;

    private Playlist playlist;

    public TestPlaylistController() {}

    public TestPlaylistController(DBExecutor dbExecutor, JList list, Playlist playlist) {
        this.dbExecutor =dbExecutor;
        this.list = list;

        this.playlist = playlist;
        this.playlist.addListener(this);
    }

    public DefaultListModel<Song> getModel() {
        return songs;
    }


    /*

    ChangeListener

    Listen to the playlist. If the playlist is re-written
    than the JList is updated.

     */
    @Override
    public void stateChanged(ChangeEvent e) {
        Playlist playlist = (Playlist) e.getSource();

        SwingUtilities.invokeLater(() -> {
            songs.clear();
            Iterator<Song> i = playlist.iterator();
            while (i.hasNext()) {
                songs.addElement(i.next());

                Utils.Log.print(songs.lastElement().getFile());

            }
            list.revalidate();
        });

    }

    /*

    MouseAdapter

    Listen to the mouse events in the JList. When
    selection is true a popup window is shown
    whith the options:
    'play'
    'remove'
    The inner classes PlayListener or RemoveListener
    is called when an option is pushed.

     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (e.getButton() == MouseEvent.BUTTON3) {
            Song song = songs.get(list.getSelectionModel().getAnchorSelectionIndex());

            if (!list.getSelectionModel().isSelectionEmpty()) {
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
            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAYID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Utils.Log.print(getClass().toString() + " " + reply);

        }
    }

    private class RemoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ListSelectionModel model = list.getSelectionModel();
            int track = model.getAnchorSelectionIndex();
            Song song = playlist.getSong(track);
            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand(PlaylistProperties.DELETE_ID, song.getId()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Utils.Log.print(getClass().toString() + " " + reply);

        }
    }

    public DropTargetListener dropTargetListener() {
        return new DropedListener();
    }

    /*

    DropTargetListener

     */
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

                Utils.Log.print(d);
            }
        }
    }
}
