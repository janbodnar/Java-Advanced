package com.zetcode.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    @DisplayName("should calculate the correct sum")
    @ParameterizedTest(name = "{index} => x={0}, y={1}, z={2} sum={3}")
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    void sum(int x, int y, int z, int sum) {

        var mySum = MathUtils.sum(x, y, z);

        assertEquals(mySum, sum);
    }
}
