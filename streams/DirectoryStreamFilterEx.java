package com.zetcode;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryStreamFilterEx {

    public static void main(String[] args) throws IOException {

        DirectoryStream.Filter<Path> filter = file -> {
            return Files.size(file) < 100_000L && file.toString().endsWith(".jpg");
        };

        var dirName = Paths.get("C:/Users/Jano/Downloads");

        try (var paths = Files.newDirectoryStream(dirName, filter)) {

            paths.forEach(path -> System.out.println(path));
        }
    }
}
