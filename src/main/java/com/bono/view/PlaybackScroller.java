package com.bono.view;

import com.bono.api.Status;

import javax.swing.event.ChangeListener;
import java.awt.event.MouseListener;

/**
 * Created by bono on 8/18/16.
 */
public interface PlaybackScroller {

    void setMinimum(int minimum);

    void setMaximum(int maximum);

    int getMaximum();

    void setValue(int value);

    int getValue();

    void addScrollerMouseListener(MouseListener l);

    void addScrollerChangeListener(ChangeListener l);

    void removeScrollerChangeListener(ChangeListener l);
}
