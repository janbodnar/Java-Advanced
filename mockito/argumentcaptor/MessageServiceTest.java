package com.zetcode;

import com.zetcode.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MessageServiceTest {

    @Mock
    private MessageService messageService;

    @Captor
    private ArgumentCaptor<String> captor1;

    @Captor
    private ArgumentCaptor<LocalDateTime> captor2;

    @Test
    void testGetMessage() {

        messageService.getMessage("Hello!", LocalDateTime.of(2019, 8, 20, 20, 18));

        Mockito.verify(messageService).getMessage(captor1.capture(), captor2.capture());

        assertEquals("Hello!", captor1.getValue());
        assertEquals(LocalDateTime.of(2019, 8, 20, 20, 18), captor2.getValue());
    }
}
