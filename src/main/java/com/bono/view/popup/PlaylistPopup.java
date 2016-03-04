package com.bono.view.popup;

import com.bono.api.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 3/4/16.
 */
public class PlaylistPopup {

    private JPopupMenu popupMenu;
    private JMenuItem playMenu;
    private JMenuItem removeItem;

    private ListSelectionModel selectionModel;
    private DefaultListModel<Song> resultModel;

    public PlaylistPopup() {
        popupMenu = new JPopupMenu();
        playMenu = new JMenuItem("play");
        removeItem = new JMenuItem("remove");
        popupMenu.add(playMenu);
        popupMenu.add(removeItem);
    }

    public PlaylistPopup(ListSelectionModel listSelectionModel, ListModel listModel) {
        selectionModel = listSelectionModel;
        resultModel = (DefaultListModel<Song>) listModel;
    }

    public void addPlayListener(ActionListener listener) {
        playMenu.addActionListener(listener);
    }

    public void addRemoveListener(ActionListener listener) {
        removeItem.addActionListener(listener);
    }

    public void show(Component invoker, int x, int y) {
        popupMenu.show(invoker, x, y);
    }

}
