package com.bono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by hendriknieuwenhuis on 03/07/16.
 */
public class ConfigLoader {

    private static final Path DIR = Paths.get(System.getProperty("user.dir"));
    private static final Path FILE = Paths.get(System.getProperty("user.dir") + "config.file");

    private ConfigLoader() {}

    private static void createSyncDir() {
        try {
            Files.createDirectory(DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createIndexFile() {
        try {
            Files.createFile(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeConnectionConfig(List<String> list) {
        try {
            Files.write(FILE, list, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readConnectionConfig() {
        try {
            return Files.readAllLines(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
