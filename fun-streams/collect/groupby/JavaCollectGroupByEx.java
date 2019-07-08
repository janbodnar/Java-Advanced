package com.zetcode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JavaCollectGroupByEx {

    public static void main(String[] args) {

        var items = List.of("pen", "book", "pen", "coin",
                "book", "desk", "book", "pen", "book", "coin");

        Map<String, Long> result = items.stream().collect(
                Collectors.groupingBy(
                        Function.identity(), Collectors.counting()
                ));

        for (Map.Entry<String, Long> entry : result.entrySet()) {

            var key = entry.getKey();
            var value = entry.getValue();

            System.out.format("%s: %d%n", key, value);
        }
    }
}
