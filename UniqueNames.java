package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class UniqueNames {

    public static void main(String[] args) {

        List<String> names = List.of("Martin", "Lucy", "Peter",
                "Martin", "Robert", "Peter");

        List<String> uniqueNames = new ArrayList<>();

        names.forEach(e -> {

            if (!uniqueNames.contains(e)) {

                uniqueNames.add(e);
            }
        });

        System.out.println(names);
        System.out.println(uniqueNames);

    }
}
