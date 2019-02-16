package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesListEx {

    public static void main(String[] args) throws IOException {

        Files.list(Paths.get("."))
                .forEach(path -> System.out.println(path));
    }
}
