package com.zetcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class JavaWriteFile {

    public static void main(String[] args) throws IOException {
        
        Path myPath = Paths.get("src/resources/myfile.txt");
        
        List<String> lines = new ArrayList<>();
        lines.add("blue sky");
        lines.add("sweet orange");
        lines.add("fast car");
        lines.add("old book");
        
        Files.write(myPath, lines, StandardCharsets.UTF_8, 
                StandardOpenOption.CREATE);
        
        System.out.println("Data written");
    }
}
