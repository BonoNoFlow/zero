package com.bono.command;

import java.util.Arrays;

/**
 * Created by hendriknieuwenhuis on 15/08/15.
 */
public class MPDCommand implements Command {

    protected String command;
    protected String[] args;

    public MPDCommand(String command) {
        this.command = command;
    }

    public MPDCommand(String command, String arg) {
        this.command = command;
        this.args = new String[]{arg};
    }

    public MPDCommand(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    /**
     * Returns the command, request and arguments as a
     * array of bytes.
     * @return byte[]
     */
    @Override
    public byte[] getCommandBytes() {

        String outCommand = null;
        if (command != null) {
            outCommand = command;
            if (args != null) {
                for (String arg : args) {
                    outCommand = outCommand + " " + arg;
                }
            }
        } else {
            return null;
        }
        outCommand += "\n";
        System.out.println(outCommand);
        return outCommand.getBytes();
    }

    @Override
    public String getCommandString() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }


    @Override
    public String toString() {
        return "ServerCommand{" +
                "args=" + Arrays.toString(args) +
                ", request='" + command + '\'' +
                '}';
    }

}
