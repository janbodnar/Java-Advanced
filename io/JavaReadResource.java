package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JavaReadResource {
    
    public static void main(String[] args) throws IOException  {       
              
        String fileName = "src/main/resources/words.txt";
        
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        
        lines.stream().forEach(System.out::println);
        
        // Files.lines(Paths.get(fileName)).forEach(System.out::println);
    }
}
