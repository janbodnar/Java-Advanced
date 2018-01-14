package com.zetcode;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class JavaBratislavaMoscowTime {

    public static void main(String[] args) {

        ZonedDateTime zbrat = ZonedDateTime.now(ZoneId.of("Europe/Bratislava"));
        ZonedDateTime zmosc = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        System.out.println(zbrat);
        System.out.println(zmosc);

        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println(utc);
    }
}
