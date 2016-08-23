package com.bono.database;

import com.bono.IdleRunner;
import com.bono.api.ClientExecutor;
import com.bono.api.DefaultCommand;
import com.bono.api.Status;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDStatus;
import com.bono.controls.SongPlaybackScroller;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by bono on 8/20/16.
 */
public class TestScroller {

    JFrame frame;
    SongPlaybackScroller scroller;

    IdleRunner idleRunner;
    Status status;

    public TestScroller() {
        try {
            SwingUtilities.invokeAndWait(() ->{
                frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                scroller = new SongPlaybackScroller();
                frame.getContentPane().add(scroller);
                frame.pack();
                frame.setVisible(true);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);
        idleRunner = new IdleRunner(clientExecutor);
        idleRunner.addListener(eventObject -> {
            try {
                status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        idleRunner.start();
        status = new Status();
        status.addListener(scroller);
        try {
            status.populate(clientExecutor.execute(new DefaultCommand(MPDStatus.STATUS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        scroller.addValueListener(e -> {
            JSlider s = (JSlider) e.getSource();
            if (!s.getValueIsAdjusting()) {
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.SEEKCUR, Integer.toString(s.getValue())));
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        //scroller.resetScroller(status);
    }



    public static void main(String[] args) {
        TestScroller t = new TestScroller();
        t.init();
    }
}
