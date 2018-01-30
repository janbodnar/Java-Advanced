package com.zetcode;

import com.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OpenCSVWriteEx2 {

    public static void main(String[] args) throws IOException {

        String[] items1 = {"book", "coin", "pencil"};
        String[] items2 = {"pen", "chair", "lamp"};
        String[] items3 = {"ball", "bowl", "spectacles"};

        List<String[]> entries = new ArrayList<>();
        entries.add(items1);
        entries.add(items2);
        entries.add(items3);

        String fileName = "src/main/resources/items.csv";

        try (FileOutputStream fos = new FileOutputStream(fileName);
                OutputStreamWriter osw = new OutputStreamWriter(fos, 
                        StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(osw)) {
            
            writer.writeAll(entries);
        }
    }
}
