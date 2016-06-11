package com.bono.directory;

import com.bono.api.*;
import com.bono.playlist.PlaylistPresenter;

/**
 * Created by hendriknieuwenhuis on 11/06/16.
 */
public class SongTester {

    private Status status;

    private Song song;

    private DBExecutor dbExecutor = new DBExecutor("192.168.2.4", 6600);
    private PlaylistPresenter playlistPresenter = new PlaylistPresenter(dbExecutor, null);

    public SongTester() {

        status = new Status();
        updateStatus();

        try {
            //song = new Song();
            //song.populate(dbExecutor.execute(new DefaultCommand(Playlist.PLAYLISTID, status.getSongid())));
            song = (Song) playlistPresenter.getSong("11");
            System.out.println(song.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateStatus() {
        try {
            status.populateStatus(dbExecutor.execute(new DefaultCommand(Status.STATUS)));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        new SongTester();
    }
}
