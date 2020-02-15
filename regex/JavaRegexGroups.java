package com.zetcode;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexGroups {

    public static void main(String[] args) {

        var sites = List.of("webcode.me", "zetcode.com", "freebsd.org",
                "netbsd.org");

        Pattern p = Pattern.compile("(\\w+)\\.(\\w+)");

        for (var site: sites)
        {
            Matcher matcher = p.matcher(site);

            while (matcher.find()) {

                System.out.println(matcher.group(0));
                System.out.println(matcher.group(1));
                System.out.println(matcher.group(2));
            }

            System.out.println("*****************");
        }
    }
}
