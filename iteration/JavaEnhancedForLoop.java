package com.zetcode;

import java.util.Arrays;
import java.util.List;

// Java 5 introduced enhanced-for loop. The creation of the iterator and 
// calls to the hasNext() and next() methods are not explicit, but they 
// still take place behind the scenes. 

public class JavaEnhancedForLoop {

    public static void main(String[] args) {
        
        List<String> items = Arrays.asList("coin", "ball", "lamp", "spoon");
        
        for (String item: items) {
            
            System.out.println(item);
        }
    }
}
