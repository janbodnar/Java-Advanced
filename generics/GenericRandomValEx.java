package com.zetcode;

import java.util.List;
import java.util.Random;

public class GenericRandomValEx {

    public static void main(String[] args) {

        var words = List.of("rock", "sky", "blue", "ocean", "falcon");
        var vals = List.of(2, 3, 4, 5, 6, 7, 8);

        var e1 = getRandomElement(words);
        System.out.println(e1);

        var e2 = getRandomElement(vals);
        System.out.println(e2);
    }

    private static <T> T getRandomElement(List<T> list) {

        var random = new Random();
        int idx = random.nextInt(list.size());
        return list.get(idx);
    }
}
