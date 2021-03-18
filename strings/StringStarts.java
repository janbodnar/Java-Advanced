package com.zetcode;

public class StringStarts {

    public static void main(String[] args) {

        var words = "club\nsky\nblue\ncup\ncoin\nnew\ncent\nowl\nfalcon\nwar\nice";

        var wstream = words.lines();
        wstream.forEach(word -> {

            if (word.startsWith("c")) {
                System.out.println(word);
            }
        });
    }
}
