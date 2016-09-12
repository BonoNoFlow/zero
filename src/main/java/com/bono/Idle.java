package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;

/**
 * Created by bono on 8/21/16.
 */
public class Idle extends Thread {

    private List<ChangeListener> listeners = new ArrayList<>();

    private Endpoint endpoint;

    private Properties properties;

    private Status status;

    private boolean running = true;

    public Idle(MPDClient mpdClient) {
        this.status = mpdClient.getStatus();
    }
    public Idle(Properties properties) {
        super();
        this.properties = properties;
    }

    @Override
    public void run() {
        super.run();

        while (running) {
            Collection<String> response = new ArrayList<>();
            endpoint = new Endpoint(properties.getProperty(ConfigLoader.HOST),
                    Integer.parseInt(properties.getProperty(ConfigLoader.PORT)));
            try {
                response = endpoint.command(new DefaultCommand(MPDStatus.IDLE));
            } catch (ACKException e) {
                e.printStackTrace();
            }catch (SocketException e) {
                // do nothing.
            } catch (IOException e) {
                e.printStackTrace();
            }

            // when idle is closed the listeners should not be triggered.
            if (running) {
                Iterator<String> i = response.iterator();
                while (i.hasNext()) {
                    String[] s = i.next().split(" ");

                    Iterator<ChangeListener> iL = listeners.iterator();
                    while (iL.hasNext()) {
                        iL.next().stateChanged(new ChangeEvent(s[1]));
                    }
                }
            }
        }
    }

    public void close() {
        running = false;
        if (endpoint != null) {
            try {
                endpoint.closeEndpoint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addListener(ChangeListener l) {
        listeners.add(l);
    }

    public void removeListeners() {
        listeners.clear();

    }

    public void removeListener(ChangeListener l) {
        for (ChangeListener c : listeners) {
            if (c.equals(l)) {
                listeners.remove(c);
            }
        }
    }
}
