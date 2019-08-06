package com.zetcode.service;

import java.time.LocalDateTime;

public class MessageService {

    public String getMessage(String message, LocalDateTime dateTime) {

        return String.format("%s %s", message, dateTime);
    }
}
