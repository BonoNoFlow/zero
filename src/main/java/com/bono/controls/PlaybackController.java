package com.bono.controls;

import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.events.PropertyEvent;
import com.bono.events.PropertyListener;
import com.bono.models.Property;
import com.bono.models.ServerStatus;
import com.bono.properties.PlayerProperties;
import com.bono.view.ControlView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hendriknieuwenhuis on 01/03/16.
 */
public class PlaybackController implements ActionListener, PropertyListener {

    private ControlView controlView;
    private DBExecutor dbExecutor;
    private ServerStatus serverStatus;

    public PlaybackController(DBExecutor dbExecutor, ControlView controlView, ServerStatus serverStatus) {
        this.dbExecutor = dbExecutor;
        this.controlView = controlView;
        this.serverStatus = serverStatus;
        init();
    }

    private void init() {
        this.serverStatus.getStatus().getStateProperty().addPropertyListeners(this);
        this.controlView.addPreviousListener(this);
        this.controlView.addPlayListener(this);
        this.controlView.addNextListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String reply = "";

        switch (e.getActionCommand()) {
            case PlayerProperties.PREVIOUS:
                //printActionCommand(e.getActionCommand());

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PREVIOUS));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                System.out.println(reply);
                break;
            case PlayerProperties.PAUSE:
                if (serverStatus.getStatus().getState().equals(PlayerProperties.STOP)) {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PLAY));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (serverStatus.getStatus().getState().equals(PlayerProperties.PAUSE)) {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PAUSE, "0"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        reply = dbExecutor.execute(new MPDCommand(PlayerProperties.PAUSE, "1"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                //printActionCommand(e.getActionCommand());
                break;
            case PlayerProperties.NEXT:

                try {
                    reply = dbExecutor.execute(new MPDCommand(PlayerProperties.NEXT));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                System.out.println(reply);
                //printActionCommand(e.getActionCommand());
                break;
            default:
                break;
        }
    }

    @Override
    public void propertyChange(PropertyEvent propertyEvent) {
        Property<String> property = (Property<String>) propertyEvent.getSource();
        printActionCommand(property.getValue());
    }

    private void printActionCommand(String value) {
        System.out.println("ActionCommand: " + value);
    }
}
