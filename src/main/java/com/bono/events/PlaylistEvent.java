package com.bono.events;

import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 12/09/15.
 */
public class PlaylistEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PlaylistEvent(Object source) {
        super(source);
    }
}
