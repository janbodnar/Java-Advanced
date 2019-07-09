package com.zetcode;

import java.util.Map;

public class HashMapEnhancedForEx {

    public static void main(String[] args) {

        Map<String, String> users = Map.of(
                "Jane Smith", "janesmith@example.com",
                "John Doe", "jdoe@example.com",
                "Peter Black", "peterblack@example.com"
        );

        for (Map.Entry<String, String> entry : users.entrySet()) {
            System.out.printf("%s %s%n", entry.getKey(), entry.getValue());
        }
    }
}
