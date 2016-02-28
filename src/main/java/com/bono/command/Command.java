package com.bono.command;

/**
 * Created by hendriknieuwenhuis on 14/08/15.
 */
public interface Command {

    byte[] getCommandBytes();

    String getCommandString();

}
