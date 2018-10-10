package com.zetcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JavaStreamMapEx4 {

    public static void main(String[] args) throws IOException {

        var lines = Files.readAllLines(Path.of("src/resources/numbers.csv"));

        var vals = lines.stream().map(line-> Arrays.asList(line.split(",")))
                .flatMap(Collection::stream).mapToInt(Integer::valueOf)
                .boxed().collect(Collectors.toList());

        System.out.println(vals);
    }
}
