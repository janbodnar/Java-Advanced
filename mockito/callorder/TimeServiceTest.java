package com.zetcode.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TimeServiceTest {

    @Mock
    private TimeService timeService;

    private LocalTime timeVal;
    private LocalDate dateVal;
    private LocalDateTime dateTimeVal;

    @Before
    public void setUp(){

        timeVal  = LocalTime.now();
        dateVal  = LocalDate.now();
        dateTimeVal  = LocalDateTime.now();
    }

    @Test
    void testOrderMethodCalls() {

        when(timeService.getTime()).thenReturn(timeVal);
        when(timeService.getDate()).thenReturn(dateVal);
        when(timeService.getDateTime()).thenReturn(dateTimeVal);

        assertEquals(timeService.getTime(), timeVal);
        assertEquals(timeService.getDate(), dateVal);
        assertEquals(timeService.getDateTime(), dateTimeVal);

        InOrder inOrder = inOrder(timeService);

        inOrder.verify(timeService).getTime();
        inOrder.verify(timeService).getDate();
        inOrder.verify(timeService).getDateTime();
    }
}
