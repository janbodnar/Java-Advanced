package com.zetcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JavaLambdaEx5 {

    public static void main(String[] args) {

        List<User> users = Arrays.asList(
                new User("Joe", "joe34@gmail.com"),
                new User("Pete", "pete252@gmail.com"),
                new User("Robert", "bobby34@gmail.com")
        );
        
        Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        System.out.println(users);
        
        Collections.sort(users, (a, b) -> a.getEmail().compareToIgnoreCase(b.getEmail()));
        System.out.println(users);
    }
}
