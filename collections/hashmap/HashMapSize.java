package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class HashMapSize {

    public static void main(String[] args) {

        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");

        int size = capitals.size();

        System.out.printf("The size of the HashMap is %d%n", size);

        capitals.remove("pol");
        capitals.remove("ita");

        size = capitals.size();

        System.out.printf("The size of the HashMap is %d%n", size);
    }
}
