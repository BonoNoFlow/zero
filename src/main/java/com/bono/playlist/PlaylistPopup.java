package com.bono.playlist;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bono on 8/17/16.
 */
public class PlaylistPopup {

    private PlaylistView playlistView;
    private JPopupMenu popupMenu;

    private ClientExecutor clientExecutor;
    private Playlist playlist;
    private PlaylistTableModel playlistTableModel;
    private PlaylistModel playlistModel;

    private MPDClient mpdClient;

    int selectionAmount;

    public PlaylistPopup(ClientExecutor clientExecutor, PlaylistView playlistView, PlaylistModel playlistModel) {
        this.clientExecutor = clientExecutor;
        this.playlistView = playlistView;
        this.playlistModel = playlistModel;
        this.selectionAmount = playlistView.getSelectedRows().length;
        if (selectionAmount == 1) {
            createSingleSelection();
        } else if (selectionAmount > 1) {
            createMultipleSelection();
        }
    }

    public PlaylistPopup(ClientExecutor clientExecutor, PlaylistView playlistView, PlaylistTableModel playlistTableModel) {
        this.clientExecutor = clientExecutor;
        this.playlistView = playlistView;

        this.playlistTableModel = playlistTableModel;
        this.selectionAmount = playlistView.getSelectedRows().length;
        if (selectionAmount == 1) {
            createSingleSelection();
        } else if (selectionAmount > 1) {
            createMultipleSelection();
        }

    }

    public PlaylistPopup(MPDClient mpdClient, PlaylistView playlistView, PlaylistModel playlistModel) {
        this.mpdClient = mpdClient;
        this.playlist = mpdClient.getPlaylist();
        this.playlistView = playlistView;
        this.playlistModel = playlistModel;
        this.selectionAmount = playlistView.getSelectedRows().length;
        if (selectionAmount == 1) {
            createSingleSelection();
        } else if (selectionAmount > 1) {
            createMultipleSelection();
        }
    }

    public void show(int x, int y) {
        if (popupMenu != null) {
            popupMenu.show(playlistView.getComponent(), x, y);
        }
    }

    private void createSingleSelection() {
        popupMenu = new JPopupMenu();
        JMenuItem play = new JMenuItem("play");
        play.addActionListener(play());
        popupMenu.add(play);
        JMenuItem remove = new JMenuItem("remove");
        remove.addActionListener(remove());
        popupMenu.add(remove);
    }

    private void createMultipleSelection() {
        popupMenu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("remove");
        remove.addActionListener(remove());
        popupMenu.add(remove);
        JMenuItem crop = new JMenuItem("crop");
        crop.addActionListener(crop());
        popupMenu.add(crop);
    }

    private ActionListener play() {
        return event -> {
            int selected = playlistView.getSelectedRows()[0];

            Song song = playlistModel.getElementAt(selected);
            try {
                mpdClient.getPlayer().playId(song.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    private ActionListener remove() {
        return event -> {
            Playlist.CommandList commandList = playlist.sendCommandList();
            for (int i : playlistView.getSelectedRows()) {
                Song song = playlistModel.getElementAt(i);
                commandList.add(MPDPlaylist.DELETE_ID, Integer.toString(song.getId()));
            }
            try {
                commandList.send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    private ActionListener crop() {
        return event -> {
            Playlist.CommandList commandList = playlist.sendCommandList();
            for (int i = 0; i < playlist.getSize(); i++) {
                if (playlistView.isRowSelected(i)) {
                    continue;
                }
                Song song = playlistModel.getElementAt(i);
                commandList.add(MPDPlaylist.DELETE_ID, Integer.toString(song.getId()));
            }
            try {
                commandList.send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
    }
}
