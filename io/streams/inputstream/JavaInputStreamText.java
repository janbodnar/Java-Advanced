package com.zetcode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// InputStream is a source for reading data. A stream 
// can represent various kinds of sources, including disk files, devices, other programs, and memory arrays. 

public class JavaInputStreamText {

    public static void main(String[] args) throws IOException {

        String fileName = "src/resources/thermopylae.txt";

        try (InputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis,
                        StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {

            br.lines().forEach(line -> System.out.println(line));
        }
    }
}
