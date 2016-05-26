package com.bono;

import com.bono.api.*;


/**
 * Created by hendriknieuwenhuis on 10/05/16.
 */
public class IdleRunner {

    private Thread thread;
    private Idle idle;

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

    public void removeListeners() {
        idle.removeListeners();
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
