package com.zetcode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

public class SevenByteBufferChannel {

    public static void main(String[] args) throws IOException {

        try (var src = Channels.newChannel(new FileInputStream("src/resources/data1.txt"))) {

            try (var dest = Channels.newChannel(new FileOutputStream("src/resources/data2.txt"))) {

                var buf = ByteBuffer.allocate(7);

                while (src.read(buf) != -1) {

                    // flips the buffer from read to write (and vice versa)
                    buf.flip();
                    dest.write(buf);
                    buf.clear();
                }
            }
        }
    }
}
