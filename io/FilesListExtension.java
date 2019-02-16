package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesListExtension {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home")
                + System.getProperty("file.separator") + "Downloads";

        Files.list(Paths.get(homeDir)).filter(path -> path.toString().endsWith(".pdf"))
                .forEach(System.out::println);
    }
}
