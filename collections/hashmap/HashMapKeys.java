package com.zetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMapKeys {

    public static void main(String[] args) {

        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");  

        Set<String> keys = capitals.keySet();

        keys.forEach(System.out::println);
    }
}
