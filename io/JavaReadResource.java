package com.zetcode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JavaReadResource {
    
    public static void main(String[] args) throws URISyntaxException, IOException {        
              
        String fileName = "src/main/resources/words.txt";
        
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        
        lines.stream().forEach(System.out::println);
    }
}
