package com.zetcode;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CopyFileChannel {
    
    public static void main(String[] args) throws IOException {

        var source = new RandomAccessFile("src/resources/bugs.txt", "r");
        var dest = new RandomAccessFile("src/resources/bugs2.txt", "rw");

        try (var sfc = source.getChannel();
             var dfc = dest.getChannel()) {

            dfc.transferFrom(sfc, 0, sfc.size());
        }
    }
}
