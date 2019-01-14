package com.zetcode.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MySelectionSortTest {

    private final int N = 10;

    private int[] vals = new int[N];

    @BeforeEach
    void beforeEach(RepetitionInfo info) {

        System.out.printf("Test #%d%n", info.getCurrentRepetition());

        var r = new Random(System.nanoTime());

        for (int i=0; i< N; i++) {
            vals[i] = r.nextInt(100);
        }
    }

    @RepeatedTest(value = 40, name = "#{displayName} {currentRepetition}/{totalRepetitions}")
    @DisplayName("should sort values")
    void doSort() {

        System.out.println(Arrays.toString(vals));

        var sorted = MySelectionSort.doSort(vals);
        Arrays.sort(vals);

        System.out.println(Arrays.toString(sorted));

        assertEquals(sorted, vals);
    }
}
