package com.zetcode;

public class StringMatch {

    public static void main(String[] args) {

        var words = """
                book
                bookshelf
                bookworm
                bookcase
                bookish
                bookkeeper
                booklet
                bookmark
                """;

        var wstream = words.lines();

        wstream.forEach(word -> {
            if (word.matches("book(worm|mark|keeper)?")) {
                System.out.println(word);
            }
        });
    }
}
