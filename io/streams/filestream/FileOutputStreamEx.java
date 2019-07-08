package com.zetcode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStreamEx {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String fileName = "/home/janbodnar/tmp/newfile.txt";
        
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            
            String text = "Today is a beautiful day";
            byte[] mybytes = text.getBytes();
            
            fos.write(mybytes);
        }
    }
}
