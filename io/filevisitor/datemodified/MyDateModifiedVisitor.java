package com.zetcode;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class MyDateModifiedVisitor extends SimpleFileVisitor<Path> {

    private final List<Path> collectedPaths = new LinkedList<>();
    private Instant instant;

    @Override
    public FileVisitResult visitFile(Path path,
                                     BasicFileAttributes basicFileAttributes) {

        var fileTime = basicFileAttributes.lastModifiedTime();
        var isAfter = fileTime.toInstant().isAfter(instant);

        if (isAfter) {

            this.collectedPaths.add(path);
        }
        return FileVisitResult.CONTINUE;
    }

    public void setInstant(Instant instant) {

        this.instant = instant;
    }

    public List<Path> getFiles() {

        return collectedPaths;
    }
}