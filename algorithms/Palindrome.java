package com.zetcode;

// A palindrome is a word, number, phrase, or other sequence of characters
// which reads the same backward as forward, such as madam or racecar

public class Palindrome {

    public static void main(String[] args) {

        System.out.println(isPalindrome("radar"));
        System.out.println(isPalindrome("kayak"));
        System.out.println(isPalindrome("forest"));
    }

    private static boolean isPalindrome(String original) {

        String reversed = new StringBuilder(original).reverse().toString();
        return original.equals(reversed);
    }
}
