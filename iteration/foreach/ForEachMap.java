package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class ForEachMap {

    public static void main(String[] args) {
        
        Map<String, Integer> items = new HashMap<>();

        items.put("coins", 3);
        items.put("pens", 2);
        items.put("keys", 1);
        items.put("sheets", 12);

        items.forEach((k, v) -> {
            System.out.printf("%s : %d%n", k, v);
        });
    }
}
