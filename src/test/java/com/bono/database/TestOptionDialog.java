package com.bono.database;

import com.bono.api.ClientExecutor;
import com.bono.api.DefaultCommand;
import com.bono.api.Status;
import com.bono.api.protocol.MPDStatus;
import com.bono.controls.PlaybackOptions;
import com.bono.view.PlaybackOptionsView;

/**
 * Created by bono on 8/12/16.
 */
public class TestOptionDialog {

    Status status;

    ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);

    public TestOptionDialog() {
        queryStatus();
        PlaybackOptions playbackOptions = new PlaybackOptions(clientExecutor, status);
    }

    private void queryStatus() {
        status = new Status();
        try {
            status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestOptionDialog();
    }
}
