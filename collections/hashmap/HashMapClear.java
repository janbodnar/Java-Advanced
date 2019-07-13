package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class HashMapClear {

    public static void main(String[] args) {

        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");

        capitals.clear();

        if (capitals.isEmpty()) {

            System.out.println("The map is empty");
        } else {
            System.out.println("The map is not empty");
        }
    }
}
