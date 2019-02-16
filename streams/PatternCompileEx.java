package com.zetcode;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatternCompileEx {

    public static void main(String[] args) {

        var phoneNumber = "202-555-0154";

        var output = Pattern.compile("-")
                .splitAsStream(phoneNumber)
                .collect(Collectors.toList());

        output.forEach(System.out::println);
    }
}
