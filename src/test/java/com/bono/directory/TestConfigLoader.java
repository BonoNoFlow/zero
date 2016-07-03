package com.bono.directory;

import com.bono.ConfigLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */
public class TestConfigLoader {

    public static void main(String[] args) {

        List<String> conf = Arrays.asList(
                "HOST 192.168.2.4",
                "PORT 6600"
        );

        try {
            //ConfigLoader.createSyncDir();
            //ConfigLoader.createIndexFile();
            ConfigLoader.writeConnectionConfig(conf);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
