package com.zetcode;

public class RecursionEx {

    public static void main(String[] args) {

        printWord("rock", 10);
    }

    public static void printWord(String word, int count) {

        System.out.printf("%d %s%n", count, word);

        if (count > 1) {

            printWord(word, count - 1);
        }
    }
}
