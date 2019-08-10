package com.zetcode.renderer;

import com.zetcode.provider.MessageProvider;

import javax.inject.Inject;

public class ConsoleMessageRenderer implements MessageRenderer {

    @Inject
    private MessageProvider messageProvider;

    public void render() {

        var message = messageProvider.getMessage();

        System.out.println(message);
    }
}
