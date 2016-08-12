package com.bono.database;

import com.bono.ConfigLoader;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */
public class TestConfigLoader {

    public static void main(String[] args) {

        ConfigLoader configLoader = new ConfigLoader();
        List<String> config;
        try {
            config = configLoader.loadConfig();
        } catch (NoSuchFileException nsf) {
            //configLoader.showDialog("config please!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
