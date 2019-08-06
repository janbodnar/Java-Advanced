package com.zetcode;

import com.zetcode.service.MessageService;

import java.time.LocalDateTime;

public class ArgumentCaptorEx {

    public static void main(String[] args) {

        var messageService = new MessageService();
        var message = messageService.getMessage("Hello there", LocalDateTime.now());

        System.out.println(message);
    }
}
