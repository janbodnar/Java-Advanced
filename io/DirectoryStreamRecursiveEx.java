package com.zetcode;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DirectoryStreamRecursiveEx {

    private static List<Path> paths = new ArrayList<>();

    private static List<Path> walk(Path path) throws IOException {

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    walk(entry);
                }
                paths.add(entry);
            }
        }

        return paths;
    }

    public static void main(String[] args) throws IOException {

        var myPath = Paths.get("C:/Users/Jano/Downloads");

        var paths = walk(myPath);

        paths.forEach(path -> System.out.println(path));
    }
}
