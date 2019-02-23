package com.zetcode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class Car {
    
    private final String name;
    private final String model;
    private final int price;
    private final String[] colours;

    public Car(String name, String model, int price, String[] colours) {
        this.name = name;
        this.model = model;
        this.price = price;
        this.colours = colours;
    }

    @Override
    public String toString() {
        return "Car{" + "name=" + name + ", model=" + model + 
                ", price=" + price + ", colours=" + Arrays.toString(colours) + '}';
    }
}

public class GsonDataBindApiRead {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String fileName = "src/main/resources/cars.json";
        Path path = Paths.get(fileName);
        
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            List<Car> cars = gson.fromJson(reader, 
                    new TypeToken<List<Car>>(){}.getType());
            
            cars.forEach(System.out::println);
        }        
    }
}
