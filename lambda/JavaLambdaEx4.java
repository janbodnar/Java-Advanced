package com.zetcode;

import java.util.Arrays;
import java.util.List;

public class JavaLambdaEx4 {

    public static void main(String[] args) {
        
        List<String> words = Arrays.asList("astronaut", "actor", "pen", 
                "book", "story", "cold", "house", "eagle", "cloud");
        
        words.stream().filter(word -> word.matches("[abc].*"))
                .forEach(e -> System.out.println(e));
    }
}
