package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class JavaStreamReduceNofCharacters {
    
    public static void main(String[] args) {
        
        List<String> words = new ArrayList<>();
        words.add("coin");
        words.add("pet");
        words.add("tennis");
        words.add("sky");
        
        int n = words.stream().mapToInt(String::length).reduce(0, (l1, l2) -> l1 + l2);
        
        System.out.printf("The count of characters in the list: %d%n", n);
    }
}
