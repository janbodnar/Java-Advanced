package com.zetcode.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class HelloServiceTest {

    @Mock
    private HelloService helloService;

    @Test
    void testHelloMessage() {

        when(helloService.getMessage()).thenReturn("Hello!");
        assertEquals(helloService.getMessage(), "Hello!");
    }
}
