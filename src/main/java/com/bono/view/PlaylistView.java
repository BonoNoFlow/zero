package com.bono.view;

import com.bono.view.renderers.PlayingRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;

/**
 * Created by bono on 8/17/16.
 */
public interface PlaylistView {

    void setModel(ListModel model);

    ListSelectionModel getSelectionModel();

    int[] getSelectedRows();

    Component getComponent();

    boolean isRowSelected(int row);

    void addDropTargetListener(DropTargetListener l);

    void addMouseListener(MouseListener l);

    void addTransferHandler(TransferHandler t);

    PlayingRenderer getPlayingRenderer();

    void redraw();
}
