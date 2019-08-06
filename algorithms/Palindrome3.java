package com.zetcode;

// A palindrome is a word, number, phrase, or other sequence of characters
// which reads the same backward as forward, such as madam or racecar

public class Palindrome3 {

    public static void main(String[] args) {

        System.out.println(isPalindrome("radar"));
        System.out.println(isPalindrome("kayak"));
        System.out.println(isPalindrome("forest"));
    }

    private static boolean isPalindrome(String original) {

        char[] data = original.toCharArray();

        int i = 0;
        int j = data.length - 1;

        while (j > i) {

            if (data[i] != data[j]) {
                return false;
            }

            ++i;
            --j;
        }
        
        return true;
    }
}
