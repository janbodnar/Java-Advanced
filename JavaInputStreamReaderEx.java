package com.zetcode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaInputStreamReaderEx {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String fileName = "src/main/resources/russiantext.txt";

        try (FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {

            String line;
            
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
