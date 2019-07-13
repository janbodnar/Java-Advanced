package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class HashMapReplace {

    public static void main(String[] args) {

        Map<String, String> capitals = new HashMap<>();

        capitals.put("day", "Monday");
        capitals.put("country", "Poland");
        capitals.put("colour", "blue");

        capitals.replace("day", "Sunday");
        capitals.replace("country", "Russia", "Great Britain");
        capitals.replace("colour", "blue", "green");

        capitals.entrySet().forEach(System.out::println);
    }
}
