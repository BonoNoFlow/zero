package com.bono;

import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.api.MPDEndpoint;
import com.bono.api.Reply;
import com.bono.api.Config;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hendriknieuwenhuis on 08/10/15.
 */
public class Idle implements Runnable {

    public static final  String PLAYLIST = "playlist";
    public static final String PLAYER = "player";

    private List<ChangeListener> listeners = new ArrayList<>();

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private MPDStatus status;
    private Config config;


    private MPDEndpoint endpoint;

    private DBExecutor dbExecutor;

    private Reply reply;


    public Idle(Config config) {
        this.config = config;
    }


    public Idle(Config config, DBExecutor dbExecutor, MPDStatus status) {
        this.config = config;
        this.dbExecutor = dbExecutor;
        this.status = status;
    }


    /*
    public Idle(Config config, DBExecutor dbExecutor, MPDStatus status, PlaylistController playlistController) {
        this(config, dbExecutor, status);
        this.playlistController = playlistController;
    }*/



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

                /*
                switch (line[1]) {
                    case PLAYLIST:
                        //if (playlistController == null) {
                        //    break;
                        //}
                        //playlistController.update();

                        break;
                    case PLAYER:
                        //System.out.println("player!");
                        break;
                    default:
                        break;
                }*/
                callListeners(line[1]);
            }

            // updateStatus();
        }
    }

    private void callListeners(String message) {
        System.out.println("Idle update: " + message + "\n");
        Iterator<ChangeListener> i = listeners.iterator();
        while (i.hasNext()) {
            i.next().stateChanged(new ChangeEvent(message));
        }
    }

    public void endIdleThread() {
        try {
            endpoint.closeEndpoint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    private void updatePlaylist() {

    }

    private void updateStatus() {
        //status.clear();

    }
}
