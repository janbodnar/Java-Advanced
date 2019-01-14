package com.zetcode;

import com.zetcode.utils.StringUtils;

import java.util.List;

public class Application {

    public static void main(String[] args) {

        var words = List.of("rodor", "book", "Sator Arepo Tenet Opera Rotas",
                "kayak", "madam", "level");

        words.forEach(w -> {

            if (StringUtils.isPalindrome(w)) {
                System.out.printf("%s is a palindrome%n", w);
            } else {
                System.out.printf("%s is not a palindrome%n", w);
            }
        });
    }
}
