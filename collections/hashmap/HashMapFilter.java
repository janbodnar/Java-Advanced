package com.zetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HashMapFilter {

    public static void main(String[] args) {

        Map<String, String> capitals = new HashMap<>();

        capitals.put("svk", "Bratislava");
        capitals.put("ger", "Berlin");
        capitals.put("hun", "Budapest");
        capitals.put("czk", "Prague");
        capitals.put("pol", "Warsaw");
        capitals.put("ita", "Rome");

        Map<String, String> filteredCapitals = capitals.entrySet().stream()
                .filter(map -> map.getValue().length() == 6)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        filteredCapitals.entrySet().forEach(System.out::println);
    }
}
