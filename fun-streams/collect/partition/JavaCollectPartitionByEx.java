package com.zetcode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaCollectPartitionByEx {

    public static void main(String[] args) {

        Map<Boolean, List<User>> statuses =
                users().stream().collect(Collectors.partitioningBy(User::isSingle));

        System.out.println(statuses);
    }

    private static List<User> users() {

        return List.of(
            new User("Julia", false),
            new User("Jake", false),
            new User("Mike", false),
            new User("Robert", true),
            new User("Maria", false),
            new User("Peter", true)
        );
    }
}
