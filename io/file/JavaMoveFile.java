package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaMoveFile {

    public static void main(String[] args) throws IOException {
        
        Path myPath = Paths.get("src/resources/myfile.txt");
        Path myPath2 = Paths.get("src/resources/myfile2.txt");
        
        Files.move(myPath, myPath2);
        
        System.out.println("File moved");
    }
}
