package com.zetcode;

public class MutableImmutableEx {
    
    public static void main(String[] args) {
        
        var word = "rock";
        var word2 = word.replace('r', 'd');

        System.out.println(word2);

        var builder = new StringBuilder("rock");
        builder.replace(0, 1, "d");

        System.out.println(builder);
    }
}
