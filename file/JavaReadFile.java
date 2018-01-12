package com.zetcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JavaReadFile {

    public static void main(String[] args) throws IOException {
        
        Path myPath = Paths.get("src/resources/bugs.txt");
        
        List<String> lines = Files.readAllLines(myPath, StandardCharsets.UTF_8);
        
        lines.forEach(line -> System.out.println(line));
    }
}
