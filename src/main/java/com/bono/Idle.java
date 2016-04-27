package com.bono;

import com.bono.api.*;

/**
 * Created by hendriknieuwenhuis on 08/10/15.
 */
public class Idle implements Runnable {

    public static final String PLAYLIST = "playlist";
    public static final String PLAYER = "player";

    private Endpoint endpoint;

    private Status status;

    private StatusControl statusControl;

    private DBExecutor dbExecutor;

    public Idle(Config config, Status status, StatusControl statusControl) {
        endpoint = new Endpoint(config);
        //this.dbExecutor = dbExecutor;
        this.status = status;
        this.statusControl = statusControl;
    }

    @Override
    public void run() {
        String reply = "";
        while (true) {
            try {
                reply = endpoint.command(new DefaultCommand(StatusControl.IDLE));
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(getClass().getName() + " " + reply);

            try {
                status.populateStatus(statusControl.status());
            } catch (ACKException ack) {
                ack.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
