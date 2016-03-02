package com.bono;

import com.bono.api.Status;

/**
 * Created by hendriknieuwenhuis on 02/03/16.
 */
public class MPDStatus {

    private Status status;

    public void setStatus(String entry) {
        String[] stats = entry.split("\n");
        for (String stat : stats) {
            String[] state = stat.split(": ");


        }

    }
}
