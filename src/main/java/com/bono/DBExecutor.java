package com.bono;

import com.bono.models.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by hendriknieuwenhuis on 02/12/15.
 *
 * TODO --- Exceptions van ExecuteCommand moeten in deze class komen om errors te displayen!!
 * TODO --- !!!? ...moet deze class naar View module terug...
 */
public class DBExecutor {

    private ExecuteCommand executeCommand;
    private ExecutorService executor;

    public DBExecutor(Command command, Config config, ExecutorService executor) {
        this.executeCommand = new ExecuteCommand(command, new MPDEndpoint(config.getHost(), config.getPort()));
        this.executor = executor;
    }

    public String execute() {
        String reply = null;

        Future<String> future = executor.submit(executeCommand);

        try {
            reply = future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  reply;
    }
}
