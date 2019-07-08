package com.zetcode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaInputStreamBytes {

    public static void main(String[] args) throws IOException {

        String fileName = "src/resources/ball.png";

        try (InputStream is = new FileInputStream(fileName)) {
            
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            
            int i = 0;
            
            for (byte b: buffer) {
                
                if (i % 10 == 0) {
                    System.out.println();
                }
                
                System.out.printf("%02x ", b);
                
                i++;
            }
        }
        
        System.out.println();
    }
}
