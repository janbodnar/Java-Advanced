package com.zetcode;

import java.text.BreakIterator;

public class StringLength {

    public static void main(String[] args) {

        var text1 = "falcon";
        var n1 = text1.length();

        System.out.printf("%s has %d characters%n", text1, n1);

        System.out.println("----------------------------");

        var text2 = "вишня";
        var n2 = text2.length();
        System.out.printf("%s has %d characters%n", text2, n2);

        System.out.println("----------------------------");

        var text3 = "🐺🦊🦝";
        var n3 = text3.length();
        System.out.printf("%s has %d characters%n", text3, n3);

        var n3_ = graphemeLength(text3);
        System.out.printf("%s has %d characters%n", text3, n3_);

        System.out.println("----------------------------");

        var text4 = "नमस्ते";

        var n4 = text4.length();
        System.out.printf("%s has %d characters%n", text4, n4);

        var n4_ = graphemeLength(text4);
        System.out.printf("%s has %d characters%n", text4, n4_);
    }

    public static int graphemeLength(String text) {

        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(text);

        int count = 0;

        while (it.next() != BreakIterator.DONE) {
            count++;
        }

        return count;
    }
}
