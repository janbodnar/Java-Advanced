package com.zetcode;

// A palindrome is a word, number, phrase, or other sequence of characters
// which reads the same backward as forward, such as madam or racecar

import java.util.Stack;

public class Palindrome4 {

    public static void main(String[] args) {

        System.out.println(isPalindrome("radar"));
        System.out.println(isPalindrome("kayak"));
        System.out.println(isPalindrome("forest"));
    }

    private static boolean isPalindrome(String original) {

        char[] data = original.toCharArray();

        Stack<Character> stack = new Stack<>();

        for (char c: data) {

            stack.push(c);
        }

        char[] data2 = new char[data.length];

        int len = stack.size();

        for (int i = 0; i < len; i++) {

            data2[i] = stack.pop();
        }

        var reversed = new String(data2);
        
        return original.equals(reversed);
    }
}
