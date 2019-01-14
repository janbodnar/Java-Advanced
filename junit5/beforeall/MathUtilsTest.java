package com.zetcode.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    private static List<Integer> vals;

    @BeforeAll
    static void setup() {

        vals = List.of(2, 1, -2, 3, -1, 0, -1);
    }

    @Test
    @DisplayName("testing sum of values")
    void sumTest() {

        var sum = MathUtils.sum(vals);

        assertEquals(Integer.valueOf(2), sum);
    }

    @Test
    @DisplayName("should get positive values")
    void positiveTest() {

        var positiveValues = MathUtils.positive(vals);

        assertEquals(positiveValues, List.of(2, 1, 3));
    }

    @Test
    @DisplayName("should get negative values")
    void negativeTest() {

        var negativeValues = MathUtils.negative(vals);

        assertEquals(negativeValues, List.of(-2, -1, -1));
    }
}
