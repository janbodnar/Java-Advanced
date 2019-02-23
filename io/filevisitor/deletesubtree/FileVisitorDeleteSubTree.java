package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class FileVisitorDeleteSubTree {

    public static void main(String[] args) throws IOException {

        var myPath = "C:/Users/Jano/Documents/data/";
        var myVisitor = new MyFileVisitor();

        Files.walkFileTree(Paths.get(myPath), new HashSet<>(), 3, myVisitor);

    }
}