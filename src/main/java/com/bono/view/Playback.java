package com.bono.view;

import java.util.HashMap;

/**
 * Created by bono on 8/11/16.
 */
public interface Playback {

    HashMap<String, Button> getButtons();

    void setPlayingSong(String artist, String title);
}
