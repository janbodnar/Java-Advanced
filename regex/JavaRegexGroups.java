package com.zetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexGroups {

    public static void main(String[] args) {

        String content = "<p>The <code>Pattern</code> is a compiled "
                + "representation of a regular expression.</p>";

        Pattern p = Pattern.compile("(</?[a-z]*>)");

        Matcher matcher = p.matcher(content);

        while (matcher.find()) {

            System.out.println(matcher.group(1));
        }
    }
}
