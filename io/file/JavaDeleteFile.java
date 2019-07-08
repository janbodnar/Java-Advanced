package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaDeleteFile {

    public static void main(String[] args) throws IOException {
        
        Path myPath = Paths.get("src/resources/myfile.txt");
        
        boolean fileDeleted = Files.deleteIfExists(myPath);
        
        if (fileDeleted) {
            
            System.out.println("File deleted");
        } else {
            
            System.out.println("File does not exist");
        }
    }
}
