package com.bono.api;

import com.bono.config.Config;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hendriknieuwenhuis on 02/12/15.
 */
public class DBExecutor {

    private ExecutorService executor;

    private Config config;

    public DBExecutor(Config config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(10);
    }

    @Deprecated
    public DBExecutor(Command command, Config config, ExecutorService executor) {

        this.executor = executor;
    }

    public String execute(Command command) throws Exception {
        ExecuteCommand executeCommand = new ExecuteCommand(command, new MPDEndpoint(config.getHost(), config.getPort()));
        String reply = null;

        Future<String> future = executor.submit(executeCommand);


        reply = future.get();

        return  reply;
    }

    private class ExecuteCommand implements Callable<String> {

        private Command command;
        private MPDEndpoint endpoint;

        public ExecuteCommand(Command command, MPDEndpoint endpoint) {
            this.command = command;
            this.endpoint = endpoint;
        }
        @Override
        public String call() throws Exception {
            return endpoint.command(command);
        }
    }
}
