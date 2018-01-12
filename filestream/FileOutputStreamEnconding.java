package com.zetcode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileOutputStreamEnconding {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String fileName = "/home/janbodnar/tmp/newfile.txt";

        FileOutputStream fos = new FileOutputStream(fileName);
        
        try (OutputStreamWriter osw =  new OutputStreamWriter(fos, 
                StandardCharsets.UTF_8)) {

            String text = "Сегодня был прекрасный день.";

            osw.write(text);
        }
    }
}
