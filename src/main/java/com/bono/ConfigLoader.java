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

    private static final Path DIR = Paths.get(System.getProperty("user.home") + "/.zero");
    private static final Path FILE = Paths.get(DIR + "/config.file");

    private ConfigLoader() {}

    public static void createSyncDir() throws IOException {
        Files.createDirectory(DIR);
    }

    public static void createIndexFile() throws IOException {
        System.out.println(FILE.toString());
        Files.createFile(FILE);
    }

    public static void writeConnectionConfig(List<String> list) throws IOException {
        System.out.println(FILE.toString());
        Files.write(FILE, list, Charset.forName("UTF-8"));
    }

    public static List<String> readConnectionConfig() throws IOException {
        return Files.readAllLines(FILE);
    }
}
