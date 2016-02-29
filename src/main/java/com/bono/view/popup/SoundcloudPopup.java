package com.bono.view.popup;

import com.bono.Utils;
import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.soundcloud.Result;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class SoundcloudPopup {

    private JPopupMenu popupMenu;
    private JMenuItem addItem;

    private DBExecutor dbExecutor;
    private ListSelectionModel selectionModel;
    private DefaultListModel<Result> resultModel;

    public SoundcloudPopup(DBExecutor dbExecutor, JList list) {
        this.dbExecutor = dbExecutor;
        this.selectionModel = list.getSelectionModel();
        this.resultModel = (DefaultListModel<Result>) list.getModel();
        popupMenu = new JPopupMenu();
        addItem = new JMenuItem("add");

        popupMenu.add(addItem);
    }

    private class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int track = selectionModel.getAnchorSelectionIndex();

            String reply = "";
            try {
                reply = dbExecutor.execute(new MPDCommand("load", Utils.loadUrl(resultModel.get(track).getUrl())));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
