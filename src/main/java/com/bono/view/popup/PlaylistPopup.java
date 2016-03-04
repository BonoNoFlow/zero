package com.bono.view.popup;

import com.bono.api.Song;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 3/4/16.
 */
public class PlaylistPopup extends MPDPopup {

    private JMenuItem playMenu;
    private JMenuItem removeItem;

    @Deprecated private ListSelectionModel selectionModel;
    @Deprecated private DefaultListModel<Song> resultModel;

    public PlaylistPopup() {
        super();
    }

    @Override
    void init() {
        playMenu = new JMenuItem("play");
        removeItem = new JMenuItem("remove");
        popupMenu.add(playMenu);
        popupMenu.add(removeItem);
    }

    @Deprecated
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



}
