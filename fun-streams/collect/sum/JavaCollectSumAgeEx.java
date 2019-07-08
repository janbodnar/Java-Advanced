package com.zetcode;

import java.util.List;
import java.util.stream.Collectors;

public class JavaCollectSumAgeEx {

    public static void main(String[] args) {

        var cats = List.of(
                new Cat("Bella", 4),
                new Cat("Othello", 2),
                new Cat("Coco", 6)
        );

        // can be replaced with mapToInt().sum()
        var ageSum = cats.stream().collect(Collectors.summingInt(cat -> cat.getAge()));

        System.out.printf("Sum of cat ages: %d%n", ageSum);
    }
}
