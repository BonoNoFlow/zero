package com.bono.view;

import com.bono.controls.Volume;

import java.util.HashMap;

/**
 * Created by bono on 8/11/16.
 */
public interface PlaybackView {

    HashMap<String, Button> getButtons();

    Volume getVolume();

    void setPlayingSong(String artist, String title);
}
