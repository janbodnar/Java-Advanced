package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileVisitorDateModifiedEx {

    public static void main(String[] args) throws IOException {

        var myPath = "C:/Users/Jano/Documents/Data/";

        var myVisitor = new MyDateModifiedVisitor();

        var myInstant = LocalDate.of(2019, 2, 1).atStartOfDay()
                        .toInstant(ZoneOffset.UTC);

        myVisitor.setInstant(myInstant);

        Files.walkFileTree(Paths.get(myPath), myVisitor);

        List<Path> foundFiles = myVisitor.getFiles();
        int nOfFiles = foundFiles.size();

        if (nOfFiles == 0) {

            System.out.println("No files found");
        } else {

            System.out.printf("Number of files found: %d%n", nOfFiles);
            System.out.println("File names:");
            foundFiles.forEach(path -> {

                var lastModified = Instant.ofEpochMilli(path.toFile().lastModified());

                var output = DateTimeFormatter.ISO_INSTANT.format(lastModified);

                System.out.printf("File name: %s, last modified: %s%n", path, output);
            });
        }
    }
}