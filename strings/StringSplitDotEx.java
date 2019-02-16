package com.zetcode;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringSplitDotEx {

    public static void main(String[] args) {

        var address = "127.0.0.1";

//        String[] output = address.split("\\.");
        String[] output = address.split(Pattern.quote("."));

        Arrays.stream(output).forEach(part -> System.out.println(part));
    }
}
