package com.bono.view.company;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

/**
 * Created by hendriknieuwenhuis on 30/11/15.
 */
public class DataView extends JSplitPane {

    private JTree music;
    private JTable playlist;

    private TreeModel musicModel;
    private TableModel playlistModel;

    private JScrollPane musicScrollPane;
    private JScrollPane playlistScrollPane;

    public DataView(TableModel playlistModel, TreeModel musicModel) {
        super();
        this.playlistModel = playlistModel;
        this.musicModel = musicModel;
        build();
    }

    private void build() {
        music = new JTree(musicModel);
        playlist = new JTable(playlistModel);
        musicScrollPane = new JScrollPane(music, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        playlistScrollPane = new JScrollPane(playlist, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        setRightComponent(playlistScrollPane);
        setLeftComponent(musicScrollPane);
        setResizeWeight(0.1);

    }

    public void setPlaylistSelectionModel(ListSelectionModel listSelectionModel) {
        playlist.setSelectionModel(listSelectionModel);
    }

    public JTable getPlaylist() {
        return playlist;
    }
}
