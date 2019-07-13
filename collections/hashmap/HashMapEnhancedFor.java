package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class HashMapEnhancedFor {

    public static void main(String[] args) {
        
        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");  
        
        for (Map.Entry<String, String> pair: capitals.entrySet()) {
        
            System.out.format("%s: %s%n", pair.getKey(), pair.getValue());
        }
    }
}
