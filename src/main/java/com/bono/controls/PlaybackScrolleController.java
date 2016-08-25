package com.bono.controls;

import com.bono.api.*;
import com.bono.api.protocol.MPDPlayback;
import com.bono.view.PlaybackScroller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

/**
 * Created by bono on 8/21/16.
 */
public class PlaybackScrolleController extends MouseAdapter implements ChangeListener {

    private PlaybackScroller playbackScroller;

    private ClientExecutor clientExecutor;

    private Timer timer;
    private Thread thread;

    private int total = 0;
    private int played = 0;

    private ScrollerValueListener scrollerValueListener = new ScrollerValueListener();

    public PlaybackScrolleController(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
    }

    public void addScrollerView(PlaybackScroller playbackScroller) {
        this.playbackScroller = playbackScroller;
        this.playbackScroller.addScrollerMouseListener(this);
    }

    // on state play initscroller.
    // on pause scroller should be paused.
    // on stop the scroller goes to zero.

    public void resetScroller(Status object) {
        if (playbackScroller == null) {
            return;
        }
        if (object == null ) {
            return;
        }

        switch (object.getState()) {
            case Status.PLAY_STATE:
                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;
                //convertTime(total, played, object.get);
                total = (int) object.getTotalTime();
                played = (int) object.getElapsedTime();
                setValues();
                timer= new Timer();
                thread = new Thread(timer);
                thread.start();
                break;
            case Status.STOP_STATE:
                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;

                total = 0;
                played = 0;
                setValues();
                break;
            case Status.PAUSE_STATE:

                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;
               // convertTime(total, played, object.getTime());
                total = (int) object.getTotalTime();
                played = (int) object.getElapsedTime();
                setValues();
                break;
        }

    }

    private void setValues() {
        if (SwingUtilities.isEventDispatchThread()) {
            playbackScroller.setMinimum(0);
            playbackScroller.setMaximum(total);
            playbackScroller.setValue(played);
        } else {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    playbackScroller.setMinimum(0);
                    playbackScroller.setMaximum(total);
                    playbackScroller.setValue(played);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }



    private void convertTime(int total, int played, String time) {
        String[] timeA = time.split(":");
        this.total = Integer.parseInt(timeA[1]);
        this.played = Integer.parseInt(timeA[0]);
    }

    @Override
    public void stateChanged(EventObject eventObject) {
        Status status = (Status) eventObject.getSource();
        resetScroller(status);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        playbackScroller.addScrollerChangeListener(scrollerValueListener);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        playbackScroller.removeScrollerChangeListener(scrollerValueListener);
    }


    /*
        Timer class pushes the scroler one second further
        every second till maximum value.
         */
    private class Timer implements Runnable {

        private boolean running = true;

        public void closeTimer() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                long btime = System.currentTimeMillis();

                // push slider one second
                final int time = playbackScroller.getValue() + 1;


                if (time <= playbackScroller.getMaximum()) {
                    SwingUtilities.invokeLater(() -> {
                        playbackScroller.setValue(time);
                    });
                }

                long etime = System.currentTimeMillis();
                long stime = 1000 - (etime - btime);
                try {
                    Thread.sleep(stime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Changelistener for the slider.
    private class ScrollerValueListener implements javax.swing.event.ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider s = (JSlider) e.getSource();
            if (!s.getValueIsAdjusting()) {
                try {
                    clientExecutor.execute(new DefaultCommand(MPDPlayback.SEEKCUR, Integer.toString(s.getValue())));
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }
}
