package com.bono.api;

import com.bono.api.Command;
import com.bono.api.MPDEndpoint;

import java.util.concurrent.Callable;

/**
 * Created by hendriknieuwenhuis on 07/10/15.
 */
public class ExecuteCommand implements Callable<String> {

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
