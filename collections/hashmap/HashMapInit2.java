package com.zetcode;

import java.util.HashMap;
import java.util.Map;

// up to Java 8
public class HashMapInit2 {

    public static void main(String[] args) {

        Map<String, String> countries = new HashMap<>() {{
            put("de", "Germany");
            put("sk", "Slovakia");
            put("ru", "Russia");
        }};

        System.out.println(countries);
    }
}
