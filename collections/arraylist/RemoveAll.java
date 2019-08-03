package com.zetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveAll {

    public static void main(String[] args) {

        List<String> letters = new ArrayList<>();
        letters.add("a");
        letters.add("b");
        letters.add("c");
        letters.add("a");
        letters.add("d");

        System.out.println(letters);

        letters.removeAll(Collections.singleton("a"));
        System.out.println(letters);
    }
}
