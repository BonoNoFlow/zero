package com.bono.api;

import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 04/03/16.
 */
public class Reply implements Iterable {

    public static final String SPLIT_LINE = ": ";

    private String[] response;

    public Reply(String reply) {
        response = reply.split("\n");
    }

    @Override
    public Iterator iterator() {
        return new ReplyIterator();
    }


    private class ReplyIterator implements Iterator {

        private int count = 0;

        @Override
        public boolean hasNext() {
            if (count < response.length) {
                return true;
            }
            return  false;
        }

        @Override
        public Object next() {
            return response[count++];
        }
    }

}
