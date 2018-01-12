package com.zetcode;

import java.io.FileWriter;
import java.io.IOException;

// FileWriter has limitation, it uses only default encoding

public class JavaFileWriterEx {

    public static void main(String[] args) throws IOException {
        
        try (FileWriter writer = new FileWriter("src/resources/myfile.txt")) {
            writer.write("Today is a sunny day");
        }
    }
}
