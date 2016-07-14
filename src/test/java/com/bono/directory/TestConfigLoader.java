package com.bono.directory;

import com.bono.Application;
import com.bono.ConfigLoader;
import com.bono.api.Endpoint;
import com.bono.view.ConnectionDialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
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
            configLoader.showDialog("config please!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
