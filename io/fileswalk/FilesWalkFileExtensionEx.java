package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FilesWalkFileExtensionEx {

    public static void main(String[] args) throws IOException {

        var dirName = "C:/Users/Jano/Downloads";

        try (Stream<Path> paths = Files.walk(Paths.get(dirName), 2)) {
            paths.map(path -> path.toString()).filter(f -> f.endsWith(".pdf"))
                    .forEach(System.out::println);
        }
    }
}
