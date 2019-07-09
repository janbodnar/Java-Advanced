package com.zetcode;

import java.util.Iterator;
import java.util.Map;

public class HashMapIteratorEx {

    public static void main(String[] args) {

        Map<String, String> users = Map.of(
                "Jane Smith", "janesmith@example.com",
                "John Doe", "jdoe@example.com",
                "Peter Black", "peterblack@example.com"
        );

        Iterator it = users.entrySet().iterator();

        while (it.hasNext()) {

            var entry = (Map.Entry) it.next();
            System.out.printf("%s %s%n", entry.getKey(), entry.getValue());
        }
    }
}
