package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;

public class JavaGetFileOwner {

    public static void main(String[] args) throws IOException {
        
        Path myPath = Paths.get("src/resources/bugs.txt");
        
        UserPrincipal userPrincipal = Files.getOwner(myPath);
        String owner = userPrincipal.getName();
        
        System.out.println(owner);
    }
}
