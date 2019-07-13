package com.zetcode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HashMapValues {

    public static void main(String[] args) {

        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");  
        
        Collection<String> vals = capitals.values();
        
        vals.forEach(System.out::println);
    }
}
