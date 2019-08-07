package com.zetcode.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class StringUtilsTest {

    @Mock
    private StringUtils utils;

    @Test
    void testReverseWithNull() {

        when(utils.reverse(null))
                .thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> utils.reverse(null));
    }

    @Test
    void testReverse() {

        when(utils.reverse("falcon"))
                .thenReturn("noclaf");

        assertEquals(utils.reverse("falcon"), "noclaf");
    }
}
