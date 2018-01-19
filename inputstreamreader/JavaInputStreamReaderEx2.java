package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaInputStreamReaderEx2 {

    public static void main(String[] args) throws IOException {

        try (BufferedReader bin
                = new BufferedReader(new InputStreamReader(System.in, 
                        StandardCharsets.UTF_8))) {

            String line;

            System.out.print("Give me a cookie: ");
            
            while (!(("cookie").equals(line = bin.readLine()))) {
                
                System.out.println(line);
                System.out.print("Give me a cookie: ");
            }
        }
    }
}
