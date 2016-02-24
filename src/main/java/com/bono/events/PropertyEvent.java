package com.bono.events;

import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 02/09/15.
 */
public class PropertyEvent extends EventObject {


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PropertyEvent(Object source) {
        super(source);
    }


}
