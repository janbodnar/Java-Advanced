package com.zetcode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GsonTreeModelRead {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String fileName = "src/main/resources/cars.json";
        Path path = Paths.get(fileName);

        try (Reader reader = Files.newBufferedReader(path, 
                StandardCharsets.UTF_8)) {

            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);

            JsonArray array = tree.getAsJsonArray();

            for (JsonElement element : array) {

                if (element.isJsonObject()) {

                    JsonObject car = element.getAsJsonObject();

                    System.out.println("********************");
                    System.out.println(car.get("name").getAsString());
                    System.out.println(car.get("model").getAsString());
                    System.out.println(car.get("price").getAsInt());

                    JsonArray cols = car.getAsJsonArray("colors");

                    cols.forEach(col -> {
                        System.out.println(col);
                    });
                }
            }
        }
    }
}
