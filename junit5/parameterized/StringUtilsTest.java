package com.zetcode.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = { "racecar", "radar", "level", "refer", "deified", "civic" })
    void isPalindrome(String word) {

        assertTrue(StringUtils.isPalindrome(word));
    }

}

