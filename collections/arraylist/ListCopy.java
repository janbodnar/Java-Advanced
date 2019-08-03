package com.zetcode;

import java.util.List;

public class ListCopy {

    public static void main(String[] args) {

        var words = List.of("forest", "wood", "eagle", "sky", "cloud");
        System.out.println(words);

        var words2 = List.copyOf(words);
        System.out.println(words2);
    }
}
