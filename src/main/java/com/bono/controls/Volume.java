package com.bono.controls;

import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

/**
 * Created by bono on 8/18/16.
 */
public interface Volume {

    void setVolume(int volume);

    void addChangeListener(ChangeListener l);
}
