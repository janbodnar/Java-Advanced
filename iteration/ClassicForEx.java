package com.zetcode;

import java.util.List;

public class ClassicForEx {

    public static void main(String[] args) {

        var items = List.of("coin", "ball", "lamp", "spoon");

        for (int i=0; i<items.size(); i++) {

            System.out.println(items.get(i));
        }
    }
}
