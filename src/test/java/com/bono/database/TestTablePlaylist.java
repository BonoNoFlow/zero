package com.bono.database;

import com.bono.PlaylistPresenter;
import com.bono.PlaylistTableModel;
import com.bono.api.*;
import com.bono.api.protocol.MPDPlaylist;
import com.bono.soundcloud.SoundcloudController;
import com.bono.view.CurrentPlaylistView;
import com.bono.view.SongCellRenderer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bono on 8/18/16.
 */
public class TestTablePlaylist {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);


        Playlist playlist = new Playlist();
        playlist.addSongListener(new SoundcloudController(clientExecutor));

        PlaylistPresenter playlistPresenter = new PlaylistPresenter(clientExecutor);



        List<String> response = new ArrayList<>();

        try {
            response = clientExecutor.execute(new DefaultCommand(MPDPlaylist.PLAYLISTINFO));
            //playlist.clear();
            playlist.populate(response);
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CurrentPlaylistView view = new CurrentPlaylistView();
        PlaylistTableModel playlistTableModel = new PlaylistTableModel(playlist);
        view.setModel(playlistTableModel);



        view.getColumn(0).setCellRenderer(new SongCellRenderer());
        view.getColumn(1).setCellRenderer(new SongCellRenderer());
        //TableColumn songColumn = new TableColumn(0);
        //songColumn.setCellRenderer(new SongCellRenderer());
        //model.addColumn(songColumn, firstColumn);
        //view.setModel(model);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
