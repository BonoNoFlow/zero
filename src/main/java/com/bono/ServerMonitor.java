package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by bono on 8/24/16.
 */
public class ServerMonitor extends Thread {

    private List<ChangeListener> listeners = new ArrayList<>();

    private Status status = new Status();

    private Endpoint endpoint;

    private Properties properties;

    private boolean running = true;

    @Override
    public void run() {
        super.run();
    }

    public void updateStatus() {
        try {
            status.populate(new Endpoint((String) properties.get(ConfigLoader.HOST),
                    Integer.parseInt((String) properties.get(ConfigLoader.PORT)), 4000)
                    .command(new DefaultCommand(MPDStatus.STATUS), 4000));
        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
