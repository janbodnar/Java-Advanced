package com.zetcode;

import java.util.List;

// Java 5 introduced enhanced-for loop. The creation of the iterator and 
// calls to the hasNext() and next() methods are not explicit, but they 
// still take place behind the scenes. 

public class EnhancedForLoopEx {

    public static void main(String[] args) {

        var items = List.of("coin", "ball", "lamp", "spoon");

        for (String item: items) {

            System.out.println(item);
        }
    }
}
