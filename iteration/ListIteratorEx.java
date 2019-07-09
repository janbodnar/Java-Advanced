package com.zetcode;

import java.util.List;

// Iterator brings the Iterator design pattern, which is a common behavioral
// pattern used to access the elements of a collection object in sequential
// manner without any need to know its underlying representation.

public class ListIteratorEx {

    public static void main(String[] args) {

        var items = List.of("coin", "ball", "lamp", "spoon");

        var it = items.iterator();

        while (it.hasNext()) {

            System.out.println(it.next());
        }
    }
}
