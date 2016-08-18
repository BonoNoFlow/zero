package com.bono.controls;

import com.bono.api.ChangeListener;
import com.bono.api.ClientExecutor;
import com.bono.api.Status;

import javax.swing.*;
import java.util.EventObject;

/**
 * Created by bono on 8/18/16.
 *
 * This class contains the logic to scroll through the current
 * playing song. Also it sets the slider to scroll to the elapsed
 * play time.
 */
public class SongPlaybackScroller extends JSlider implements ChangeListener, PlaybackScroller {

    private Timer timer;
    private Thread thread;

    public SongPlaybackScroller() {
        super(JSlider.HORIZONTAL);
    }

    // on state play initscroller.
    // on pause scroller should be paused.
    // on stop the scroller goes to zero.
    @Override
    public void resetScroller(Status object) {
        if (object == null ) {
            return;
        }

        final Status finalObject = object;
        switch (object.getState()) {
            case "play":
                thread = null;
                timer = null;
                SwingUtilities.invokeLater(() -> {
                    setMinimum(0);
                    setMaximum(Integer.parseInt(finalObject.getTime()));
                    setValue(Integer.parseInt(finalObject.getElapsed()));
                });
                timer= new Timer();
                thread = new Thread(timer);
                thread.start();
                break;
            case "stop":
                thread = null;
                timer = null;
                SwingUtilities.invokeLater(() -> {
                    setMinimum(0);
                    setMaximum(0);
                    setValue(0);
                });
                break;
            case "pause":
                thread = null;
                timer = null;
                SwingUtilities.invokeLater(() -> {
                    setMinimum(0);
                    setMaximum(Integer.parseInt(finalObject.getTime()));
                    setValue(Integer.parseInt(finalObject.getElapsed()));
                });
                break;
        }

    }

    // listens to status.
    @Override
    public void stateChanged(EventObject eventObject) {
        resetScroller((Status) eventObject.getSource());
    }


    /*
    Timer class pushes the scroler one second further
    every second till maximum value.
     */
    private class Timer implements Runnable {

        @Override
        public void run() {
            while (true) {
                long btime = System.currentTimeMillis();

                // push slider one second
                final int time = getValue() + 1;

                if (time <= getMaximum()) {
                    SwingUtilities.invokeLater(() -> {
                        setValue(time);
                    });
                }

                long etime = System.currentTimeMillis();
                long stime = etime - btime;
                try {
                    Thread.sleep(stime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
