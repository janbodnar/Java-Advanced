package com.zetcode;

public class VowelsConsonantsEx {

    // count the number of vowels and consonants in 
    // all words of a list
    public static void main(String[] args) {
        
        String[] words = {"sun", "forest", "wood", "rocks"};

        int vowels = 0;
        int consonants=0;

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            for (int j = 0; j < word.length(); j++) {

                char c = word.charAt(j);

                if (c == 'a' || c == 'e' || c == 'y' || c == 'i' || c == 'u' || c =='o') {
                    vowels++;
                } else {
                    consonants++;
                }
            }
        }

        System.out.printf("Vowels: %d%n", vowels);
        System.out.printf("Consonants: %d%n", consonants);
    }
}
