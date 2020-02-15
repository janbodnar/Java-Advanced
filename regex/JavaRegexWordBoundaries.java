package com.zetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexWordBoundaries {

    public static void main(String[] args) {

        var text = "This island is beautiful";

        Pattern p = Pattern.compile("\\bis\\b");
        Matcher matcher = p.matcher(text);

        while (matcher.find())
        {
            System.out.printf("%s at %d", matcher.group(), matcher.start());
        }
    }
}
