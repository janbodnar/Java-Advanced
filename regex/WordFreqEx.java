package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordFreqEx {

    public static void main(String[] args) throws IOException {

        var fileName = "src/resources/the-king-james-bible.txt";
        var text = Files.readString(Path.of(fileName));

        var regex = "[a-zA-Z']+";
        var p = Pattern.compile(regex);
        var matcher = p.matcher(text);

        var words = matcher.results();

        var res = words.collect(Collectors.groupingBy(MatchResult::group,
                Collectors.counting()));

        res.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .forEach((e -> System.out.printf("%s %d%n", e.getKey(), e.getValue())));
    }
}
