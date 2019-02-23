package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileVisitorSizeEx {

    public static void main(String[] args) throws IOException {

        var myPath = "C:/Users/Jano/Documents/Data/";

        var myVisitor = new MyFileSizeVisitor();
        long limit = 1024 * 1024; // one megabyte

        myVisitor.setSizeLimit(limit);

        Files.walkFileTree(Paths.get(myPath), myVisitor);

        List<Path> foundFiles = myVisitor.getFiles();
        int nOfFiles = foundFiles.size();

        if (nOfFiles == 0) {

            System.out.println("No files found");
        } else {

            System.out.printf("Number of files found: %d%n", nOfFiles);
            System.out.println("File names:");
            foundFiles.forEach(path -> System.out.println(path));
        }

    }
}