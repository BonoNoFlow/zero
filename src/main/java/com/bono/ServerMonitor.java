package com.bono;

import com.bono.api.*;
import com.bono.api.protocol.MPDStatus;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;

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
        while (running) {
            Collection<String> response = new ArrayList<>();
            endpoint = new Endpoint(properties.getProperty(ConfigLoader.HOST),
                    Integer.parseInt(properties.getProperty(ConfigLoader.PORT)));
            try {
                response = endpoint.command(new DefaultCommand(MPDStatus.IDLE), 4000);
            } catch (ACKException e) {
                e.printStackTrace();
            }catch (SocketException e) {
                // do nothing.
            } catch (IOException e) {
                e.printStackTrace();
            }

            // when idle is closed the listeners should not be triggered.
            if (running) {
                // first update the status object
                updateStatus();

                // second fire the listeners
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

    public void addMonitorListener(ChangeListener l) {
        listeners.add(l);
    }

    public void addStatusListener(ChangeListener l) {
        status.addListener(l);
    }
    public boolean removeStatusListener(ChangeListener l) {
        return status.removeListener(l);
    }

    public void removeMonitorListeners() {
        listeners.clear();

    }

    public void removeMonitorListener(ChangeListener l) {
        for (ChangeListener c : listeners) {
            if (c.equals(l)) {
                listeners.remove(c);
            }
        }
    }
}
