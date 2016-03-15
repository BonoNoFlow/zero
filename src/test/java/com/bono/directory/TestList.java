package com.bono.directory;

import com.bono.Idle;
import com.bono.MPDStatus;
import com.bono.api.*;
import com.bono.config.Config;
import com.bono.playlist.MPDPlaylist;
import com.bono.playlist.PlaylistController;
import com.bono.soundcloud.AdditionalTrackInfo;
import com.bono.view.PlaylistCellRenderer;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Iterator;


/**
 * Created by hendriknieuwenhuis on 13/03/16.
 */
public class TestList {

    public static final String CLIENTID = "93624d1dac08057730320d42ba5a0bdc";

    JList list;

    Idle idle;

    MPDStatus status;

    Playlist playlist;

    TestPlaylistController playlistController;

    DBExecutor dbExecutor;

    Config config = new Config("192.168.2.4", 6600);

    public TestList() {
        dbExecutor = new DBExecutor(config);

        // init status.
        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand("status"));
        } catch (Exception e0) {
            e0.printStackTrace();
        }
        status = new MPDStatus(dbExecutor);
        //status.addListener(this);
        status.setStatus(reply);

        // setup idle.
        idle = new Idle(config);
        idle.addListener(status);
        idle.addListener(new IdlePlaylistListener());
        new Thread(idle).start();

        // init playlist.
        playlist = new Playlist();
        //idle.addListener(playlist);
        playlist.addListener(new AdditionalTrackInfo(CLIENTID));



        // init view.
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JScrollPane pane = new JScrollPane();
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            list = new JList();
            playlistController = new TestPlaylistController(dbExecutor, list, playlist);
            //playlist.addListener(playlistController);
            list.addMouseListener(playlistController);
            list.setModel(playlistController.getModel());
            list.setCellRenderer(new PlaylistCellRenderer());
            initPlaylist();
            pane.getViewport().add(list);
            frame.getContentPane().add(pane);
            frame.pack();
            frame.setVisible(true);
        });


    }

    // listener updates playlist model.
    private class IdlePlaylistListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            String message = (String) e.getSource();
            if (message.equals(Idle.PLAYLIST)) {
                initPlaylist();
                System.out.println("Playlist initiated!");
                //list.c
            }
        }

    }

    // listener updates playlist view.
    private class PlaylistViewListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            System.out.println("Playlist listener TRIGGER!!!");

            //!!!!!!!!!!!!!!! Hier gaat het mis!!!!!!!!!!!!!!
            MPDPlaylist playlist1 = (MPDPlaylist) e.getSource();
            System.out.println(playlist1.getClass() + " " + playlist1.getSize());
            System.out.println(playlist.getClass() + " " + playlist.getSize());

            for (int i = 0; i < playlist.getSize(); i++) {
                System.out.println(playlist.getSong(i).getFile());
            }
            System.out.println("Playlist listener DONE!!!");
            /*
            if (list != null) {
                SwingUtilities.invokeLater(() -> {
                    list.setModel(playlist.getModel());
                    //list = new JList(playlist.getSongVector());
                });
            }*/
        }
    }

    private void initPlaylist() {
        // init playlist.
        System.out.println("going to initiate playlist");
        String reply = "";
        try {
            reply = dbExecutor.execute(new MPDCommand("playlistinfo"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        playlist.populate(reply);
    }

    public static void main(String[] args) {
        new TestList();
    }
}
