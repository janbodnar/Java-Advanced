package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class RemoveIf {

    public static void main(String[] args) {

        List<String> words = new ArrayList<>();
        words.add("forest");
        words.add("flower");
        words.add("grass");
        words.add("wood");
        words.add("sky");
        words.add("rock");
        words.add("river");

        words.removeIf(e -> e.length() == 5);

        System.out.println(words);
    }
}
