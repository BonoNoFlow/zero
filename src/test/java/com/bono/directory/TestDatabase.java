package com.bono.directory;

import com.bono.api.ClientExecutor;
import com.bono.api.DefaultCommand;
import com.bono.api.protocol.MPDDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bono on 8/9/16.
 */
public class TestDatabase {

    ClientExecutor clientExecutor = new ClientExecutor("192.168.2.4", 6600, 4000);

    public TestDatabase() {
        List<String> artists = new ArrayList<>();
        try {
            artists = clientExecutor.execute(new DefaultCommand(MPDDatabase.FIND, "any", "\"speedy j\""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<String> i = artists.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }

        clientExecutor.shutdownExecutor();
    }

    public static void main(String[] args) {
        new TestDatabase();
    }
}
