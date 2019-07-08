package com.zetcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileInputStreamEx2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String fileName = "/home/janbodnar/tmp/smallfile.txt";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            int i; 
            
            while ((i = fis.read()) != -1) {
                System.out.print((char) i);
            }
        }        
        
        System.out.println();
    }
}
