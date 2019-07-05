package com.zetcode;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

// creating a Map from a List; each word is given 
// a unique Id
public class JavaCollectToMapEx {

    public static void main(String[] args) {

        var words = List.of("marble", "coin", "forest", "falcon",
                "sky", "cloud", "eagle", "lion");

        Map<UUID, String> result = words.stream()
                .collect(Collectors.toMap(e -> UUID.randomUUID(),
                        Function.identity()));

        System.out.println(result);
    }
}
