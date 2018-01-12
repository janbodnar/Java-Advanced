package com.zetcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class JavaCopyFile {

    public static void main(String[] args) throws IOException {

        File source = new File("src/resources/bugs.txt");
        File dest = new File("src/resources/bugs2.txt");

        Files.copy(source.toPath(), dest.toPath(), 
                StandardCopyOption.REPLACE_EXISTING);
    }
}
