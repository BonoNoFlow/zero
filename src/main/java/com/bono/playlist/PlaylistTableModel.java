package com.bono.playlist;

import com.bono.api.Playlist;
import com.bono.api.Song;

import javax.swing.table.AbstractTableModel;

/**
 * Created by bono on 8/18/16.
 */
public class PlaylistTableModel extends AbstractTableModel {

    private Playlist playlist;

    private String[] names = {"song", "time"};

    public PlaylistTableModel(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Song.class;
    }

    @Override
    public int getRowCount() {
        if (playlist == null) {
            return 0;
        }
        return playlist.getSize();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return playlist.getSong(rowIndex);
    }

    @Override
    public String getColumnName(int column) {
        return names[column];
    }
}
