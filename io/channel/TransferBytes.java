package com.zetcode;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TransferBytes {

    public static void main(String[] args) throws IOException {

        var srcFileName = "src/resources/beginning.txt";
        var destFileName = "src/resources/beginning2.txt";

        try (var srcFile = new RandomAccessFile(srcFileName, "rw");
             var src = srcFile.getChannel()) {

            try (var destFile = new RandomAccessFile(destFileName, "rw");
                 var dest = destFile.getChannel()) {

                long position = 0;
                long count = src.size();

                dest.transferFrom(src, position, count);
            }
        }
    }
}
