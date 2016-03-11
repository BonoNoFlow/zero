package com.bono.directory;


import com.bono.MPDDirectory;
import com.bono.api.DBExecutor;
import com.bono.api.MPDCommand;
import com.bono.config.Config;

import javax.swing.*;

/**
 * Created by hendriknieuwenhuis on 07/03/16.
 */
public class TestDir {

    DBExecutor dbExecutor;

    MPDDirectory mpdDirectory;

    ViewDir viewDir;

    Directory directory;

    TestMPDDirectory testMPDDirectory;

    public TestDir() {
        Config config = new Config("192.168.2.4", 6600);
        dbExecutor = new DBExecutor(config);
        //String reply = null;
        //try {
        //    reply = dbExecutor.execute(new MPDCommand("lsinfo"));
            //System.out.println(s);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        //mpdDirectory = new MPDDirectory(reply);
        //directory = new Directory();
        //directory.populate(reply);

        mpdDirectory = new MPDDirectory(dbExecutor);

        //testMPDDirectory = new TestMPDDirectory(dbExecutor);
        //testMPDDirectory.populate(reply, testMPDDirectory.getRoot());
        SwingUtilities.invokeLater(() -> {
            viewDir = new ViewDir(mpdDirectory.getModel());
            //viewDir.addTreeWillExpandListener(testMPDDirectory);
            viewDir.addMouseListener(mpdDirectory);
        });
    }

    public static void main(String[] args) {
        new TestDir();

    }
}
