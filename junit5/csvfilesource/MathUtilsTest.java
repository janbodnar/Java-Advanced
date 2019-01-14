package com.zetcode.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    void sum(int x, int y, int z, int sum) {

        var mySum = MathUtils.sum(x, y, z);

        assertEquals(mySum, sum);
    }
}
