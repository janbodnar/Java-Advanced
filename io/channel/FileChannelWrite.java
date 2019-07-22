package com.zetcode;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FileChannelWrite {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/thermopylae.txt";
        var myfile = new RandomAccessFile(fileName, "rw");

        try (var fileChannel = myfile.getChannel()) {

            var text = getText();

            byte[] byteData = text.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(byteData);
            fileChannel.write(buffer);
        }
    }

    private static String getText() {

        var lineSeparator = System.getProperty("line.separator");
        var sb = new StringBuilder();

        sb.append("The Battle of Thermopylae was fought between " +
                "an alliance of Greek city-states,");
        sb.append(lineSeparator);
        sb.append("led by King Leonidas of Sparta, and " +
                "the Persian Empire of Xerxes I over the");
        sb.append(lineSeparator);
        sb.append("course of three days, during the " +
                "second Persian invasion of Greece.");

        return sb.toString();
    }
}
