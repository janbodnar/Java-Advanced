package com.zetcode;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class JavaBratislavaMoscowTimeEx {

    public static void main(String[] args) {

        var zbrat = ZonedDateTime.now(ZoneId.of("Europe/Bratislava"));
        var zmosc = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        System.out.println(zbrat);
        System.out.println(zmosc);

        var utc = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println(utc);
    }
}
