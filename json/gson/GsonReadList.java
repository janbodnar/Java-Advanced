package com.zetcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

class Item {
    
    private String name;
    private int quantity;

    @Override
    public String toString() {
        return "Item{" + "name=" + name + ", quantity=" + quantity + '}';
    }
}

public class GsonReadList {
    
    public static void main(String[] args) throws IOException {
        
        Gson gson = new GsonBuilder().create();
        
        try (Reader reader = new FileReader("src/main/resources/items.json")) {
            
            List<Item> items = gson.fromJson(reader, 
                    new TypeToken<List<Item>>(){}.getType());
            
            items.forEach(System.out::println);
        }
    }    
}
