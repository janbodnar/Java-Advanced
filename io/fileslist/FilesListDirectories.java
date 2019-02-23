package com.zetcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FilesListDirectories {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home");

        Files.list(new File(homeDir).toPath())
                .filter(path -> path.toFile().isDirectory())
                .forEach(System.out::println);
    }
}
