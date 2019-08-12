package com.zetcode.board;

import com.zetcode.TextModify;

import javax.enterprise.event.Observes;

public class NotifyBoard {

    public void textNotify(@Observes @TextModify String value) {

        System.out.printf("Notification: %s%n", value);
    }
}
