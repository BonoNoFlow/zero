package com.bono.playlist;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.view.PlaylistView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bono on 8/17/16.
 */
public class PlaylistPopup {

    private PlaylistView playlistView;
    private JPopupMenu popupMenu;

    private Player player;

    private Playlist playlist;

    private PlaylistModel playlistModel;

    int selectionAmount;


    public PlaylistPopup(MPDClient mpdClient, PlaylistView playlistView, PlaylistModel playlistModel) {
        this.player = mpdClient.getPlayer();
        this.playlist = mpdClient.getPlaylist();
        this.playlistView = playlistView;
        this.playlistModel = playlistModel;
        this.selectionAmount = playlistView.getSelectedRows().length;

        if (selectionAmount == 0) {
            createNoSelection();
        } else if (selectionAmount == 1) {
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

    private void createNoSelection() {
        popupMenu = new JPopupMenu();
        JMenuItem clear = buildMenuItem("clear", clear());
        popupMenu.add(clear);
    }

    private void createSingleSelection() {
        popupMenu = new JPopupMenu();
        JMenuItem play = buildMenuItem("play", play());
        popupMenu.add(play);
        JMenuItem remove = buildMenuItem("remove", remove());
        popupMenu.add(remove);
        JMenuItem clear = buildMenuItem("clear", clear());
        popupMenu.add(clear);
    }

    private void createMultipleSelection() {
        popupMenu = new JPopupMenu();
        JMenuItem remove = buildMenuItem("remove", remove());
        popupMenu.add(remove);
        JMenuItem crop = buildMenuItem("crop", crop());
        popupMenu.add(crop);
        JMenuItem clear = buildMenuItem("clear", clear());
        popupMenu.add(clear);
    }

    private JMenuItem buildMenuItem(String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(listener);
        return item;
    }

    private ActionListener play() {
        return event -> {
            int selected = playlistView.getSelectedRows()[0];

            Song song = playlistModel.getElementAt(selected);
            try {
                player.playId(song.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            playlistView.getSelectionModel().clearSelection();
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
            playlistView.getSelectionModel().clearSelection();
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
            playlistView.getSelectionModel().clearSelection();
        };
    }

    private ActionListener clear() {
        return event -> {
            try {
                playlist.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playlistView.getSelectionModel().clearSelection();
        };
    }
}
