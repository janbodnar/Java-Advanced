package com.zetcode;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class MyTextFileVisitor extends SimpleFileVisitor<Path> {

    private final List<Path> collectedPaths = new LinkedList<>();

    @Override
    public FileVisitResult visitFile(Path path,
                                     BasicFileAttributes basicFileAttributes) {

        if (path.toString().endsWith(".txt")) {

            this.collectedPaths.add(path);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

        if (dir.toString().equals("images")) {

            return FileVisitResult.SKIP_SUBTREE;
        }

        return FileVisitResult.CONTINUE;
    }

    public List<Path> getFiles() {

        return collectedPaths;
    }
}