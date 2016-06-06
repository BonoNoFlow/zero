package com.bono.executor;

import com.bono.api.Command;
import com.bono.api.CommandList;
import com.bono.api.Endpoint;
import com.bono.config.ZeroConfig;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hendriknieuwenhuis on 04/06/16.
 */
public class ZExecutor {

    private ExecutorService executor;
    private String host;
    private int port;

    /**
     * Initialize with a fixed thread pool executor of 10 threads.
     */
    public ZExecutor() {
        executor = Executors.newFixedThreadPool(10);
    }

    /**
     * The host address
     * @return String host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the host address.
     * @param host a String.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * The port value.
     * @return int port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port value.
     * @param port an int.
     */
    public void setPort(int port) {
        this.port = port;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    private class ExecuteCommand implements Callable<String> {
        private Command command;
        private CommandList commandList;
        private Endpoint endpoint;

        public ExecuteCommand(Command command, CommandList commandList, Endpoint endpoint) {
            this.command = command;
            this.commandList = commandList;
            this.endpoint = endpoint;
        }

        public String call() throws Exception {
            return this.command != null?this.endpoint.command(this.command):(this.commandList != null?this.endpoint.command(this.commandList):null);
        }
    }
}
