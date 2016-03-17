package com.bono.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 17/03/16.
 */
public class CommandList {

    private List<Command> list = new ArrayList<>();

    public void  addCommand(Command command) {
        list.add(command);
    }


}
