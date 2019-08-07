package com.zetcode;

import com.zetcode.service.MessageService;

public class VerifyNumberOfCalls {

    public static void main(String[] args) {

        var messageService = new MessageService();

        messageService.say();
        messageService.say();
        messageService.say();
    }
}
