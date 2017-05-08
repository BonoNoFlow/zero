package com.bono.database;

import com.bono.api.ChangeListener;
import com.bono.api.MPDClient;
import com.bono.api.StoredPlaylists;
import com.bono.soundcloud.SoundcloudPopup;
import com.bono.view.StoredPlaylistsPopup;
import com.bono.view.StoredPlaylistsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bono on 12/19/16.
 */
public class StoredPlaylistsController extends MouseAdapter implements ChangeListener, ActionListener {

    private MPDClient mpdClient;

    private StoredPlaylists storedPlaylists;

    private StoredPlaylistsView storedPlaylistsView;

    public StoredPlaylistsController(MPDClient mpdClient) {
        this.mpdClient = mpdClient;
    }

    public void setStoredPlaylistsView(StoredPlaylistsView storedPlaylistsView) {
        this.storedPlaylistsView = storedPlaylistsView;
        this.storedPlaylistsView.addSaveListener(this);
        this.storedPlaylistsView.addPlaylistsMouselistener(this);
    }

    public void init() {
        storedPlaylists = new StoredPlaylists(mpdClient.getClientExecutor());
        List<String> list = null;
        try {
            list = storedPlaylists.playlists();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        DefaultListModel<String> model = new DefaultListModel<>();
        Iterator<String> i = list.iterator();
        while (i.hasNext()) {
            model.addElement(i.next());
        }
        SwingUtilities.invokeLater(() -> {
            if (storedPlaylistsView != null) {
                storedPlaylistsView.setModel(model);
            }
        });
    }

    /*
        Shows a JPopupMenu that contains an 'load' function
        to load the tracks to the playlist.

        Also the result amount can be adjusted.
         */
    private void showPopup(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (e.getSource() instanceof JList) {
                JList list = (JList) e.getSource();

                if (!list.isSelectionEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        //new SoundcloudPopup(list, e, playlist);
                        new StoredPlaylistsPopup(list, e, storedPlaylists);
                    });
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

    // changelistener listens to servermonitor.
    @Override
    public void stateChanged(EventObject eventObject) {
        String s = (String) eventObject.getSource();
        //System.out.println("Playlists " + s);
        if (s.equals("stored_playlist")) {
            init();
        }
    }

    // listener for save button.
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            storedPlaylists.save(storedPlaylistsView.getSaveNameText());
        } catch (IOException ioe) {

        }
        SwingUtilities.invokeLater(() -> {
            storedPlaylistsView.clearsaveNameField();
        });
    }
}
