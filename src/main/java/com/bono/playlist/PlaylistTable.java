package com.bono.playlist;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

/**
 * Created by hendriknieuwenhuis on 19/08/16.
 */
public class PlaylistTable extends JTable {

    public PlaylistTable() {
        super();
        setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1)
            {
                if(isSelectedIndex(index0)) {
                    addSelectionInterval(index0, index1);
                }
                else {
                    removeSelectionInterval(index0, index1);
                }
            }

        });
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        //System.out.println(toggle);
        //if (toggle && isRowSelected(rowIndex)) {
        //    return;
        //}
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        super.valueChanged(e);
    }
}
