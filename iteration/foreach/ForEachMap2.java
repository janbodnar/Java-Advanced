package com.zetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ForEachMap2 {

    public static void main(String[] args) {

        HashMap<String, Integer> map = new HashMap<>();

        map.put("cups", 6);
        map.put("clocks", 2);
        map.put("pens", 12);

        Consumer<Map.Entry<String, Integer>> action = entry ->
        {
            System.out.printf("key: %s", entry.getKey());
            System.out.printf(" value: %s%n", entry.getValue());
        };

        map.entrySet().forEach(action);
    }
}
