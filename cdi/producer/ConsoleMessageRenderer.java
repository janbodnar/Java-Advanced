package com.zetcode.renderer;

import com.zetcode.producer.Message;
import com.zetcode.producer.MessageType;
import com.zetcode.provider.MessageProvider;

import javax.inject.Inject;

public class ConsoleMessageRenderer implements MessageRenderer {

    @Inject
    @Message(MessageType.TIME_MESSAGE)
    private MessageProvider messageProvider;

    public void render() {

        var msg = messageProvider.getMessage();

        System.out.println(msg);
    }
}
