package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaInputStreamReaderEx2 {

    public static void main(String[] args) throws IOException {

        try (BufferedReader bin
                = new BufferedReader(new InputStreamReader(System.in))) {

            String line;

            System.out.print("Give me a cookie: ");
            
            while (!(line = bin.readLine()).equals("cookie")) {
                
                System.out.println(line);
                System.out.print("Give me a cookie: ");
            }
        }
    }
}
