package com.bono;

/**
 * Created by hendriknieuwenhuis on 15/03/16.
 */
public class Log {

    static boolean debug = true;

    static void print(String log) {
        if (debug)
            System.out.println(log);
    }
}
