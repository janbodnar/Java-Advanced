package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexAlternation {

    public static void main(String[] args) {

        List<String> users = Arrays.asList("Jane", "Thomas", "Robert",
                "Lucy", "Beky", "John", "Peter", "Andy");

        Pattern p = Pattern.compile("Jane|Beky|Robert");

        for (String user : users) {

            Matcher m = p.matcher(user);

            if (m.find()) {
                System.out.printf("%s matches%n", user);
            } else {
                System.out.printf("%s does not match%n", user);
            }
        }
    }
}
