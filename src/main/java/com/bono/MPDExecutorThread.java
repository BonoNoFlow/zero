package com.bono;

import com.bono.models.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by hendriknieuwenhuis on 26/02/16.
 */
public class MPDExecutorThread {

    private Config config;

    private ExecutorService executorService;

    public MPDExecutorThread(Config config) {
        this.config = config;
        init();
    }

    private void init() {
        executorService = Executors.newFixedThreadPool(3);
    }

    public Config getConfig() {
        return config;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
