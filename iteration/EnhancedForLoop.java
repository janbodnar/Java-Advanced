package com.zetcode;

import java.util.Arrays;
import java.util.List;

public class EnhancedForLoop {

    public static void main(String[] args) {
        
        List<String> items = Arrays.asList("coin", "ball", "lamp", "spoon");
        
        for (String item: items) {
            
            System.out.println(item);
        }
    }
}
