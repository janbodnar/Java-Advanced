package com.zetcode;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GsonStreamApiWrite {
    
    public static void main(String[] args) throws IOException {
        
        String fileName = "src/main/resources/cars.json";
        Path path = Paths.get(fileName);
        
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(path, 
                StandardCharsets.UTF_8))) {
            
            writer.beginObject(); 
            writer.name("name").value("Audi");
            writer.name("model").value("2012");
            writer.name("price").value(22000);
            
            writer.name("colours");
            writer.beginArray();
            writer.value("gray");
            writer.value("red");
            writer.value("white");
            writer.endArray();
            
            writer.endObject();
        } 
        
        System.out.println("Data written to file");
    }
}
