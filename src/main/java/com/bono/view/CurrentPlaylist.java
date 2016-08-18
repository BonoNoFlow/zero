package com.bono.view;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;

/**
 * Created by bono on 8/17/16.
 */
public interface CurrentPlaylist {

    void setModel(TableModel model);
    TableColumn getColumn(int index);
    ListSelectionModel getSelectionModel();
    int[] getSelectedRows();
    Component getComponent();
    boolean isRowSelected(int row);
    void addDropTargetListener(DropTargetListener l);
    void addMouseListener(MouseListener l);
}
