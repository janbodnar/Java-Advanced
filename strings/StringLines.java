package com.zetcode;

public class StringLines {

    public static void main(String[] args) {

        var words = """
                club
                sky
                blue
                cup
                coin
                new
                cent
                owl
                falcon
                brave
                war
                ice
                paint
                water
                """;

        var wstream = words.lines();

        wstream.forEach(word -> {

            if (word.length() == 3) {
                System.out.println(word);
            }
        });
    }
}
