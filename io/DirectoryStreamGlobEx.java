package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryStreamGlobEx {

    public static void main(String[] args) throws IOException {

        var dirName = Paths.get("C:/Users/Jano/Downloads");

        try (var paths = Files.newDirectoryStream(dirName, "*.pdf")) {

            paths.forEach(path -> System.out.println(path));
        }
    }
}

