package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessBuilderEx {

    public static void main(String[] args) throws IOException, InterruptedException {

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cal", "2019", "-m 2");

        var process = processBuilder.start();

        var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

    }
}

   February 2019      
Su Mo Tu We Th Fr Sa  
                1  2  
 3  4  5  6  7  8  9  
10 11 12 13 14 15 16  
17 18 19 20 21 22 23  
24 25 26 27 28        
