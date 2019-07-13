package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class HashMapContainsKey {

    public static void main(String[] args) {
        
        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");

        String key1 = "ger";
        String key2 = "rus";
        
        if (capitals.containsKey(key1)) {
            
            System.out.printf("HashMap contains %s key%n", key1);
        } else {
            
            System.out.printf("HashMap does not contain %s key%n", key1);
        }
        
        if (capitals.containsKey(key2)) {
            
            System.out.printf("HashMap contains %s key%n", key2);
        } else {
            
            System.out.printf("HashMap does not contain %s key%n", key2);
        }      
    }
}
