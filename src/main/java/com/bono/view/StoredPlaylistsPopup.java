package com.bono.view;


import com.bono.api.StoredPlaylists;
import com.bono.soundcloud.Result;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by bono on 12/19/16.
 */
public class StoredPlaylistsPopup implements ActionListener {

    private static final String LOAD = "load";
    private static final String REMOVE = "remove";

    private JList list;
    private StoredPlaylists storedPlaylists;

    private JPopupMenu popupMenu;

    public StoredPlaylistsPopup(JList list, MouseEvent mouseEvent, StoredPlaylists storedPlaylists) {
        this.list = list;
        this.storedPlaylists = storedPlaylists;
        init(mouseEvent);
    }

    private void init(MouseEvent event) {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            JMenuItem loadItem = new JMenuItem(LOAD);
            loadItem.addActionListener(this);
            popupMenu.add(loadItem);
            JMenuItem removeItem = new JMenuItem(REMOVE);
            removeItem.addActionListener(this);
            popupMenu.add(removeItem);

        }
        popupMenu.show(list, event.getX(), event.getY());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        DefaultListModel<String> model = (DefaultListModel) list.getModel();
        int selected = list.getSelectedIndex();
        if (menuItem.getText().equals(LOAD)) {
            try {
                storedPlaylists.load(model.getElementAt(selected), null);
            } catch (IOException ioe) {

            }
        } else if (menuItem.getText().equals(REMOVE)) {
            try {
                storedPlaylists.remove(model.getElementAt(selected));
            } catch (IOException ioe) {

            }
        }
    }
}
