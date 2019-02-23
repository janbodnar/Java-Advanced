package com.zetcode;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class GsonToJson {

    public static void main(String[] args) {

        Map<Integer, String> colours = new HashMap<>();
        colours.put(1, "blue");
        colours.put(2, "yellow");
        colours.put(3, "green");
        
        Gson gson = new Gson();
        
        String output = gson.toJson(colours);
        
        System.out.println(output);
    }
}
