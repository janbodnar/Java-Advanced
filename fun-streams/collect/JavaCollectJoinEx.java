package com.zetcode;

import java.util.List;
import java.util.stream.Collectors;

public class JavaCollectJoinEx {

    public static void main(String[] args) {

        var words = List.of("marble", "coin", "forest", "falcon",
                "sky", "cloud", "eagle", "lion");

        var joined = words.stream().collect(Collectors.joining(","));

        System.out.printf("Joined string: %s", joined);
    }
}
