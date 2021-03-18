package com.zetcode;

public class StringUpperLower {

    public static void main(String[] args) {

        var word1 = "Cherry";

        var u_word1 = word1.toUpperCase();
        var l_word1 = u_word1.toLowerCase();

        System.out.println(u_word1);
        System.out.println(l_word1);

        var word2 = "Čerešňa";

        var u_word2 = word2.toUpperCase();
        var l_word2 = u_word2.toLowerCase();

        System.out.println(u_word2);
        System.out.println(l_word2);
    }
}
