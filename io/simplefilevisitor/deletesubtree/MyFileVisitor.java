package com.zetcode;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class MyFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path path,
                                     BasicFileAttributes basicFileAttributes) throws IOException {

        if (path.toString().endsWith(".txt")) {

            Files.delete(path);
            System.out.printf("%s deleted %n", path);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {

        if (e == null) {

            if (!Files.list(dir).findAny().isPresent()) {
                Files.delete(dir);
                System.out.printf("%s directory deleted", dir);
            }

            return FileVisitResult.CONTINUE;
        } else {
            // directory iteration failed
            throw e;
        }
    }
}