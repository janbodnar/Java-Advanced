package com.zetcode;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class MyFileSizeVisitor extends SimpleFileVisitor<Path> {

    private final List<Path> collectedPaths = new LinkedList<>();
    private long sizeLimit;

    @Override
    public FileVisitResult visitFile(Path path,
                                     BasicFileAttributes basicFileAttributes) {

        long fileSize = basicFileAttributes.size();

        if (fileSize >= sizeLimit) {

            this.collectedPaths.add(path);
        }
        return FileVisitResult.CONTINUE;
    }

    public void setSizeLimit(long bytes) {

        sizeLimit = bytes;
    }

    public List<Path> getFiles() {

        return collectedPaths;
    }
}