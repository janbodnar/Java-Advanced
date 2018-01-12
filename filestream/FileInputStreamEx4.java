package com.zetcode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileInputStreamEx4 {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String fileName = "/home/janbodnar/tmp/bigfile.txt";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), StandardCharsets.UTF_8));) {

            String line;
            
            while ((line = br.readLine()) != null) {
                
                System.out.println(line);
            }
        }

        System.out.println();
    }
}
