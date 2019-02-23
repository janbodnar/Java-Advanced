package com.zetcode;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class FileVisitorFindDirsEx {

    public static void main(String[] args) throws IOException {

        var myPath = Paths.get("C:/Users/Jano/Documents/data");

        final List<Path> directories = new LinkedList<>();

        Files.walkFileTree(myPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attrs) {

                directories.add(file);

                return FileVisitResult.CONTINUE;
            }
        });

        int nOfDirs = directories.size();

        if (nOfDirs == 0) {

            System.out.println("No subdirectories found");
        } else {

            System.out.printf("Number of subdirectories found: %d%n", nOfDirs);
            System.out.println("Subdirectory names:");
            directories.forEach(path -> System.out.println(path));
        }
    }
}
