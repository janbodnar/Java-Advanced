package com.zetcode;

import java.util.HashMap;
import java.util.Map;

public class JavaStreamFilterMapByKeys {

    public static void main(String[] args) {

        Map<String, String> hmap = new HashMap<>();
        
        hmap.put("de", "Germany");
        hmap.put("hu", "Hungary");
        hmap.put("sk", "Slovakia");
        hmap.put("si", "Slovenia");
        hmap.put("so", "Somalia");
        hmap.put("us", "United States");
        hmap.put("ru", "Russia");
        
        hmap.entrySet().stream().filter(map -> map.getKey().startsWith("s"))
                .forEach(m -> System.out.println(m));
    }
}
