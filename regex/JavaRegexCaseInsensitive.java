package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexCaseInsensitive {

    public static void main(String[] args) {

        List<String> users = Arrays.asList("dog", "Dog", "DOG", "Doggy");

        Pattern p = Pattern.compile("dog", Pattern.CASE_INSENSITIVE);

        users.forEach((user) -> {
            
            Matcher m = p.matcher(user);

            if (m.matches()) {
                System.out.printf("%s matches%n", user);
            } else {
                System.out.printf("%s does not match%n", user);
            }
        });
    }
}
