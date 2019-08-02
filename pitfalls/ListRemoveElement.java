package com.zetcode;

import java.util.ArrayList;

// java.util.ConcurrentModificationException is thrown
// either use iterator pattern, or removeIf method

public class ListRemoveElement {

    public static void main(String[] args) {

        var words = new ArrayList<>();
        words.add("forest");
        words.add("wood");
        words.add("sky");
        words.add("book");
        words.add("rock");

        for (var word : words) {

            if ("rock".equals(word)) {

                words.remove(word);
            }
        }
    }
}
