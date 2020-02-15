package com.zetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexImplicitWordBoundaries {

    public static void main(String[] args) {

        var content = """
Foxes are omnivorous mammals belonging to several genera
of the family Canidae. Foxes have a flattened skull, upright triangular ears,
a pointed, slightly upturned snout, and a long bushy tail. Foxes live on every
continent except Antarctica. By far the most common and widespread species of
fox is the red fox.""";

        Pattern p = Pattern.compile("\\w+");
        Matcher matcher = p.matcher(content);

        int count = 0;
        while (matcher.find()) {

            count++;
            System.out.println(matcher.group());
        }

        System.out.printf("There are %d words", count);
    }
}
