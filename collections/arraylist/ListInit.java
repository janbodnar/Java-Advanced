package com.zetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListInit {

    public static void main(String[] args) {

        var words = new ArrayList<String>();
        words.add("rock");
        words.add("forest");
        words.add("wood");
        words.add("river");
        System.out.println(words);

        var drinks = new ArrayList<String>() {{
            add("beer");
            add("lemonade");
            add("juice");
            add("coffee");
            add("tea");
        }};
        System.out.println(drinks);

        var colours = Arrays.asList("blue", "red", "green", "yellow");
        System.out.println(colours);

        var seasons = List.of("spring", "summer", "autumn", "winter");
        System.out.println(seasons);
    }
}
