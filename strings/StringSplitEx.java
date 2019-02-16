package com.zetcode;

import java.util.Arrays;

public class StringSplitEx {

    public static void main(String[] args) {

        var phoneNumber = "202-555-0154";

        String[] output = phoneNumber.split("-");

        Arrays.stream(output).forEach(part -> System.out.println(part));
    }
}
