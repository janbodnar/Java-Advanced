package com.zetcode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChannelCopy {

    public static void main(String[] args) throws IOException {

        var srcFile = Paths.get("src/resources/beginning.txt");
        var destFile = Paths.get("src/resources/beginning2.txt");

        try (var src = Channels.newChannel(Files.newInputStream(srcFile))) {

            try (var dest = Channels.newChannel(Files.newOutputStream(destFile))) {

                copyBytes(src, dest);
            }
        }
    }

    private static void copyBytes(ReadableByteChannel src,
                                  WritableByteChannel dest) throws IOException {

        var buf = ByteBuffer.allocateDirect(1024);

        while (src.read(buf) != -1) {

            // flip the buffer from read to write
            buf.flip();

            // Make sure that the buffer was fully drained
            while (buf.hasRemaining()) {

                dest.write(buf);
            }

            buf.clear();
        }
    }
}
