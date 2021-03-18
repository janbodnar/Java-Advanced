package com.zetcode;

public class StringEnds {

    public static void main(String[] args) {

        var words = "club\nsky\nblue\ncup\ncoin\nnew\ncent\nowl\nfalcon\nwar\nice";

        var wstream = words.lines();
        wstream.forEach(word -> {

            if (word.endsWith("n") || word.endsWith("y")) {
                System.out.println(word);
            }
        });
    }
}
