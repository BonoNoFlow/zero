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

    private Playlist playlist;

    private ClientExecutor clientExecutor;

    private Timer timer;
    private Thread thread;

    private long total = 0;
    private long played = 0;

    private ScrollerValueListener scrollerValueListener = new ScrollerValueListener();

    public PlaybackScrolleController(ClientExecutor clientExecutor, Playlist playlist) {
        this.clientExecutor = clientExecutor;
        this.playlist = playlist;
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
                total = object.getTotalTime();
                played = object.getElapsedTime();
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

                total = 0L;
                played = 0L;
                setValues();
                break;
            case Status.PAUSE_STATE:

                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;
               // convertTime(total, played, object.getTime());
                total = object.getTotalTime();
                played = object.getElapsedTime();
                setValues();
                break;
        }

    }

    private void setValues() {
        if (SwingUtilities.isEventDispatchThread()) {
            playbackScroller.setMinimum(0);
            playbackScroller.setMaximum((int) total);
            playbackScroller.setTotalTime(formattedTime(total));
            playbackScroller.setValue((int) played);
            playbackScroller.setPlayingTime(formattedTime(played));
        } else {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    playbackScroller.setMinimum(0);
                    playbackScroller.setMaximum((int) total);
                    playbackScroller.setTotalTime(formattedTime(total));
                    playbackScroller.setValue((int) played);
                    playbackScroller.setPlayingTime(formattedTime(played));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    public String formattedTime(long totalSeconds) {
        long secondsInHour = 3600L;
        long secondsInMinute = 60L;
        if(totalSeconds == -1L) {
            totalSeconds = 0L;
        }

        long hour = totalSeconds / secondsInHour;
        long remainingMin = totalSeconds % secondsInHour;
        long minutes = remainingMin / secondsInMinute;
        long seconds = remainingMin % secondsInMinute;
        String result = null;
        if(hour == 0L) {
            result = String.format("%02d:%02d", minutes, seconds);
        } else {
            result = String.format("%02d:%02d:%02d", hour, minutes, seconds);
        }

        return result;
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
                        playbackScroller.setPlayingTime(formattedTime(time));
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
