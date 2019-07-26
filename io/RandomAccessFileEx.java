package com.zetcode;

import java.io.IOException;
import java.io.RandomAccessFile;

// RandomAccessFile allows to directly manipulate
// the contents of a file
public class RandomAccessFileEx {

    public static void main(String[] args) throws IOException {

        try (var raf = new RandomAccessFile("test.txt", "rw")) {

            raf.writeBytes("golden ring");

            raf.seek(0);
            System.out.println(raf.readLine());

            raf.seek(7);
            raf.writeBytes("eagle");

            raf.seek(0);
            System.out.println(raf.readLine());
        }
    }
}
