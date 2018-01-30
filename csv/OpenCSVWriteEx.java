package com.zetcode;

import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class OpenCSVWriteEx {

    public static void main(String[] args) throws IOException {

        String[] entries = { "book", "coin", "pencil", "cup" }; 
        String fileName = "src/main/resources/items.csv";
        
        try (FileOutputStream fos = new FileOutputStream(fileName);
                OutputStreamWriter osw = new OutputStreamWriter(fos, 
                        StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(osw)) {
            
            writer.writeNext(entries);
        }        
    }
}
