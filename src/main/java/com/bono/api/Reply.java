package com.bono.api;

import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 04/03/16.
 */
public class Reply implements Iterable, Iterator {

    private String[] response;

    private int count = 0;

    public Reply(String reply) {
        response = reply.split("\n");
    }

    @Override
    public boolean hasNext() {
        if (count < response.length) {
            return true;
        }
        return  false;
    }

    @Override
    public Object next() {
        String line[] = response[count++].split(": ");
        return line[1];
    }

    @Override
    public Iterator iterator() {
        return this;
    }




}
