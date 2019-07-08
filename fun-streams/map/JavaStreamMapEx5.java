package com.zetcode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JavaStreamMapEx5 {

    public static void main(String[] args) {

        var users = List.of(new User("Peter", "programmer"),
                new User("Jane", "accountant"), new User("Robert", "teacher"),
                new User("Milan", "programmer"), new User("Jane", "designer"));

        var userNames = users.stream().map(user -> user.getName()).sorted()
                .collect(Collectors.toList());
        System.out.println(userNames);

        var occupations = users.stream().map(user -> user.getOccupation())
                .sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());

        System.out.println(occupations);
    }
}
