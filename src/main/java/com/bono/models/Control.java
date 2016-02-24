package com.bono.models;

import java.util.concurrent.ExecutorService;

/**
 * Created by hendriknieuwenhuis on 13/09/15.
 */
public abstract class Control {

    protected String host;
    protected int port;
    protected ExecutorService executorService;

    public Control() {}

    public Control(String host, int port, ExecutorService executorService) {
        this.host = host;
        this.port = port;
        this.executorService = executorService;
    }

    public abstract void init();
}
