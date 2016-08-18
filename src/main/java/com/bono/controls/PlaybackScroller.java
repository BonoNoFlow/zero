package com.bono.controls;

import com.bono.api.Status;

import javax.swing.event.ChangeListener;

/**
 * Created by bono on 8/18/16.
 */
public interface PlaybackScroller {

    void resetScroller(Status status);

    void addChangeListener(ChangeListener l);
}
