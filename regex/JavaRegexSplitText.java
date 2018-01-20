package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class JavaRegexSplitText {
    
    static int sum = 0;
        
    public static void main(String[] args) throws IOException {
        
        Path myPath = Paths.get("src/main/resources/data.csv");
        
        List<String> lines = Files.readAllLines(myPath);
       
        String regex = ",";
        
        Pattern p = Pattern.compile(regex);
        
        lines.forEach((line) -> {
            
            String[] parts = p.split(line);
            
            for (String part : parts) {
                
                String val = part.trim();
                
                sum += Integer.valueOf(val);
            }
            
        });
        
        System.out.printf("Sum of values: %d", sum);
    }
}
