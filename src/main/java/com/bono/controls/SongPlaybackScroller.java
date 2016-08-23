package com.bono.controls;

import com.bono.api.ChangeListener;
import com.bono.api.Status;
import com.bono.view.PlaybackScroller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

/**
 * Created by bono on 8/18/16.
 *
 * This class contains the logic to scroll through the current
 * playing song. Also it sets the slider to scroll to the elapsed
 * play time.
 */
public class SongPlaybackScroller extends JSlider implements ChangeListener {

    private Timer timer;
    private Thread thread;

    private javax.swing.event.ChangeListener valueListener;

    public SongPlaybackScroller() {
        super(JSlider.HORIZONTAL);
        addMouseListener(new ScrollerMouseAdapter());
    }

    // on state play initscroller.
    // on pause scroller should be paused.
    // on stop the scroller goes to zero.

    public void resetScroller(Status object) {
        if (object == null ) {
            return;
        }
        String[] time = object.getTime().split(":");
        int total = Integer.parseInt(time[1]);
        int played = Integer.parseInt(time[0]);
        //final Status finalObject = object;
        switch (object.getState()) {
            case "play":
                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        setMinimum(0);
                        setMaximum(total);
                        setValue(played);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                timer= new Timer();
                thread = new Thread(timer);
                thread.start();
                break;
            case "stop":
                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        setMinimum(0);
                        setMaximum(0);
                        setValue(0);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case "pause":
                System.out.println("paused");
                if (timer != null) {
                    timer.closeTimer();
                }
                thread = null;
                timer = null;
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        setMinimum(0);
                        setMaximum(total);
                        setValue(played);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    // listens to status.
    @Override
    public void stateChanged(EventObject eventObject) {
        resetScroller((Status) eventObject.getSource());
    }



    public void addValueListener(javax.swing.event.ChangeListener l) {
        valueListener = l;
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
                final int time = getValue() + 1;

                if (time <= getMaximum()) {
                    SwingUtilities.invokeLater(() -> {
                        setValue(time);
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


    // MouseListener adds changelistener on pressed and
    // removes it on released.
    private class ScrollerMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (valueListener != null) {
                addChangeListener(valueListener);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (valueListener != null) {
                removeChangeListener(valueListener);
            }
        }
    }

}
