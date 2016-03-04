package com.bono.view.popup;

import com.bono.Utils;
import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.soundcloud.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 29/02/16.
 */
public class SoundcloudPopup extends MPDPopup {

    //private JPopupMenu popupMenu;
    private JMenuItem addItem;

    //private DBExecutor dbExecutor;
    @Deprecated private ListSelectionModel selectionModel;
    @Deprecated private DefaultListModel<Result> resultModel;

    public SoundcloudPopup(JList list) {
        //this.dbExecutor = dbExecutor;
        this.selectionModel = list.getSelectionModel();
        this.resultModel = (DefaultListModel<Result>) list.getModel();
        popupMenu = new JPopupMenu();
        addItem = new JMenuItem("add");

        popupMenu.add(addItem);
    }

    public SoundcloudPopup() {
        super();
    }

    @Override
    void init() {
        popupMenu = new JPopupMenu();
        addItem = new JMenuItem("add");
        popupMenu.add(addItem);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);
    }

    public void addAddListener(ActionListener listener) {
        addItem.addActionListener(listener);
    }

    /*
    public void show(Component invoker, int x, int y) {
        popupMenu.show(invoker, x, y);
    }*/
}
