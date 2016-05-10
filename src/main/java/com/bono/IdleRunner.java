package com.bono;

import com.bono.api.*;
import com.bono.api.Idle;

import javax.swing.event.ChangeListener;

/**
 * Created by hendriknieuwenhuis on 10/05/16.
 */
public class IdleRunner {

    private Thread thread;
    private com.bono.api.Idle idle;

    public IdleRunner(Status status) {
        idle = new Idle(status);
        thread = new Thread(new Runner());
    }

    public void start() {
        thread.start();
    }

    public void addListener(ChangeListener listener) {
        idle.addListener(listener);
    }

    private class Runner implements Runnable {

        @Override
        public void run() {
            try {
                idle.runIdle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
