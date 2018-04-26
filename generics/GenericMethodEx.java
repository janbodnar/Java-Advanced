package com.zetcode;

import java.util.HashSet;
import java.util.Set;

public class GenericMethodEx {

    public static void main(String[] args) {
        
        Set<String> words = new HashSet<>();
        words.add("blue");
        words.add("pen");
        words.add("dog");
        words.add("narrow");
        
        Set<String> words2 = new HashSet<>();
        words.add("blue");
        words.add("pencil");
        words.add("forest");
        words.add("narrow");
        
        Set<String> res = union(words, words2);
        System.out.println(res);
    }
    
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        
        return result;
    }
}
