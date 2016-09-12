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

    private MPDClient mpdClient;

    private Endpoint endpoint;

    private boolean running = true;

    public ServerMonitor(MPDClient mpdClient) {
        this.mpdClient = mpdClient;


    }

    public void initMonitor() {
        updateStatus();
    }

    @Override
    public void run() {
        super.run();
        while (running) {
            Collection<String> response = new ArrayList<>();
            endpoint = new Endpoint(mpdClient.getHost(), mpdClient.getPort());
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

    private void updateStatus() {
        try {
            mpdClient.getStatus().populate(new Endpoint(mpdClient.getHost(), mpdClient.getPort())
                    .command(new DefaultCommand(MPDStatus.STATUS)));
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
        mpdClient.getStatus().addListener(l);
    }
    public boolean removeStatusListener(ChangeListener l) {
        return mpdClient.getStatus().removeListener(l);
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
