package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class RetainAll {

    public static void main(String[] args) {

        List<String> letters = new ArrayList<>();
        letters.add("a");
        letters.add("b");
        letters.add("c");
        letters.add("a");
        letters.add("d");

        System.out.println(letters);

        letters.retainAll(List.of("a", "b"));
        System.out.println(letters);
    }
}
