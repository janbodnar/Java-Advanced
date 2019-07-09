package com.zetcode;

import java.util.Map;

public class HashMapForEachEx {

    public static void main(String[] args) {

        Map<String, String> users = Map.of(
                "Jane Smith", "janesmith@example.com",
                "John Doe", "jdoe@example.com",
                "Peter Black", "peterblack@example.com"
        );

        users.forEach((key, value) -> System.out.printf("%s %s%n", key, value));
    }
}
