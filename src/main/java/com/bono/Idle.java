package com.bono;

import com.bono.command.*;
import com.bono.config.*;


import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hendriknieuwenhuis on 08/10/15.
 */
public class Idle implements Runnable {

    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private MPDStatus status;
    private Config config;

    private MPDEndpoint endpoint;

    DBExecutor dbExecutor;

    public Idle(Config config, DBExecutor dbExecutor, MPDStatus status) {
        this.config = config;
        this.dbExecutor = dbExecutor;
        this.status = status;
    }

    @Override
    public void run() {

        while (true) {
            endpoint = new MPDEndpoint(config.getHost(), config.getPort());
            //System.out.println("Idle start");
            String reply = null;
            try {
                reply = endpoint.command(new MPDCommand("idle"));
            } catch (SocketException se) {
                System.out.println("socket closed");
                updateStatus();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(getClass().getName() + " " + reply + " - " + Thread.activeCount());

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

    private void updateStatus() {
        //status.clear();
        try {
            status.setStatus(dbExecutor.execute(new MPDCommand("status")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
