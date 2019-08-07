package com.zetcode;

import com.zetcode.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MessageServiceTest {

    @Mock
    private MessageService messageService;

    @Test
    void testSayMessage() {

        messageService.say();
        messageService.say();
        messageService.say();

        Mockito.verify(messageService, Mockito.times(3)).say();
    }
}
