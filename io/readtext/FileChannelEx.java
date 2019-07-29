package com.zetcode;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";

        try (RandomAccessFile myFile = new RandomAccessFile(fileName, "rw");
             FileChannel inChannel = myFile.getChannel()) {

            ByteBuffer buf = ByteBuffer.allocate(48);

            int bytesRead = inChannel.read(buf);

            while (bytesRead != -1) {

                buf.flip();

                while (buf.hasRemaining()) {

                    System.out.print((char) buf.get());
                }

                buf.clear();
                bytesRead = inChannel.read(buf);
            }
        }
    }
}
