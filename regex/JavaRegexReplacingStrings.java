package com.zetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaRegexReplacingStrings {

    public static void main(String[] args) throws MalformedURLException, IOException {

        URL url = new URL("http://www.something.com");

        try (InputStreamReader isr = new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {

            String content = br.lines().collect(Collectors.joining(System.lineSeparator()));

            Pattern p = Pattern.compile("<[^>]*>");

            Matcher matcher = p.matcher(content);
            String stripped = matcher.replaceAll("");

            System.out.println(stripped);
        }
    }
}
