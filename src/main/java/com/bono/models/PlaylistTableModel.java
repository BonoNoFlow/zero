package com.bono.models;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * Created by hendriknieuwenhuis on 12/09/15.
 */
public class PlaylistTableModel extends DefaultTableModel {

    private boolean[][] editable_cells; // 2d array to represent rows and columns

    public PlaylistTableModel() {
        super();
    }

    public PlaylistTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public PlaylistTableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public PlaylistTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
        this.editable_cells = new boolean[rowCount][columnNames.length];
    }

    public PlaylistTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    public PlaylistTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        this.editable_cells = new boolean[data.length][columnNames.length];
    }

    /*
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }*/

    public void setPlaylistTableModel(Playlist playlist) {
        setRowCount(playlist.getList().size());
        for (int i = 0; i < playlist.getSize(); i++) {

            setValueAt(playlist.getList().get(i).getPos(), i, 0);
            //setCellEditable(i, 0, false);

            if (playlist.getList().get(i).getTitle() == null) {
                setValueAt(playlist.getList().get(i).getFile(), i, 1);
                //setCellEditable(i, 1, false);
                setValueAt(playlist.getList().get(i).getName(), i, 2);
                //setCellEditable(i, 2, false);
            } else {
                setValueAt(playlist.getList().get(i).getTitle(), i, 1);
                //setCellEditable(i, 1, false);
                setValueAt(playlist.getList().get(i).getArtist(), i, 2);
                //setCellEditable(i, 2, false);
            }
        }
    }

    private void setCellEditable(int row, int column, boolean value) {
        this.editable_cells[row][column] = value;
        this.fireTableCellUpdated(row, column);
    }
}
