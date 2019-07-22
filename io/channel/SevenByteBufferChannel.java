package com.zetcode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SevenByteBufferChannel {

    public static void main(String[] args) throws IOException {

        var srcFile = Paths.get("src/resources/data.txt");
        var destFile = Paths.get("src/resources/data2.txt");

        try (var src = Channels.newChannel(Files.newInputStream(srcFile))) {

            try (var dest = Channels.newChannel(Files.newOutputStream(destFile))) {

                var buf = ByteBuffer.allocate(7);

                while (src.read(buf) != -1) {

                    // flips the buffer from read to write 
                    buf.flip();

                    while (buf.hasRemaining()) {

                        dest.write(buf);
                    }

                    buf.clear();
                }
            }
        }
    }
}
