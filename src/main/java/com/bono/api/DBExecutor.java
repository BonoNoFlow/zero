package com.bono.api;

import com.bono.Utils;
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

    public String execute(Command command) throws Exception {
        ExecuteCommand executeCommand = new ExecuteCommand(command, null, new MPDEndpoint(config.getHost(), config.getPort()));
        String reply = null;
        Future<String> future = executor.submit(executeCommand);
        reply = future.get();
        return  reply;
    }

    public String executeList(CommandList commandList) {
        return null;
    }

    private class ExecuteCommand implements Callable<String> {

        private Command command;
        private CommandList commandList;
        private MPDEndpoint endpoint;

        public ExecuteCommand(Command command, CommandList commandList, MPDEndpoint endpoint) {
            this.command = command;
            this.commandList = commandList;
            this.endpoint = endpoint;
        }
        @Override
        public String call() throws Exception {

            if (command == null) {
                Utils.Log.print("command null!");
                //
            } else if (commandList == null) {
                return endpoint.command(command);
            }

            return null;
        }
    }
}
