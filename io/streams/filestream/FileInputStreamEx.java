package com.zetcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileInputStreamEx {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String fileName = "/home/janbodnar/tmp/smallfile.txt";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            char c1 = (char) fis.read();
            char c2 = (char) fis.read();
            char c3 = (char) fis.read();

            System.out.println(c1);
            System.out.println(c2);
            System.out.println(c3);
        }
    }
}
