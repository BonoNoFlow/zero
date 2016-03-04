package com.bono;

import com.bono.api.Reply;
import com.bono.command.*;
import com.bono.config.*;
import com.bono.playlist.PlaylistController;


import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hendriknieuwenhuis on 08/10/15.
 */
public class Idle implements Runnable {

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private MPDStatus status;
    private Config config;
    private PlaylistController playlistController;

    private MPDEndpoint endpoint;

    private DBExecutor dbExecutor;

    private Reply reply;

    public Idle(Config config, DBExecutor dbExecutor, MPDStatus status, PlaylistController playlistController) {
        this.config = config;
        this.dbExecutor = dbExecutor;
        this.status = status;
        this.playlistController = playlistController;
    }

    @Override
    public void run() {

        while (true) {
            endpoint = new MPDEndpoint(config.getHost(), config.getPort());
            //System.out.println("Idle start");
            //String reply = null;
            try {
                //reply = endpoint.command(new MPDCommand("idle"));
                reply = new Reply(endpoint.command(new MPDCommand("idle")));
            } catch (SocketException se) {
                System.out.println("socket closed");
                updateStatus();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(getClass().getName() + " " + reply + " - " + Thread.activeCount());

            Iterator i = reply.iterator();

            while (i.hasNext()) {

                String[] line =((String) i.next()).split(Reply.SPLIT_LINE);

                switch (line[1]) {
                    case "playlist":
                        if (playlistController == null) {
                            break;
                        }
                        playlistController.update();
                        break;
                    case "player":
                        System.out.println("player!");
                        break;
                    default:
                        break;
                }
            }

            updateStatus();
        }
    }

    public void endIdleThread() {
        try {
            endpoint.closeEndpoint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePlaylist() {

    }

    private void updateStatus() {
        //status.clear();
        try {
            status.setStatus(dbExecutor.execute(new MPDCommand("status")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
