package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileOutputStreamEnconding {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/newfile.txt";

        try (var fos = new FileOutputStream(fileName);
             var osw =  new OutputStreamWriter(fos,
                StandardCharsets.UTF_8)) {

            var text = "Сегодня был прекрасный день.";

            osw.write(text);
        }
    }
}
