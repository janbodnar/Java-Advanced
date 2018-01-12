package com.zetcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileInputStreamEx3 {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String fileName = "/home/janbodnar/tmp/bigfile.txt";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            int i = 0;

            do {

                byte[] buf = new byte[1028];
                i = fis.read(buf);
                
                String value = new String(buf, StandardCharsets.UTF_8);
                System.out.print(value);

            } while (i != -1);
        }
    }
}
