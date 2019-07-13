package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class HashMapRetrieve {

    public static void main(String[] args) {
        
        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");   

        String cap1 = capitals.get("ita");
        String cap2 = capitals.get("svk");
        
        System.out.println(cap1);
        System.out.println(cap2);
    }
}
