package com.zetcode;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class JavaCurrentDateTime {

    public static void main(String[] args) {

        Date dt = new Date();
        System.out.println(dt);

        // Java 8
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);

        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Europe/Bratislava"));
        System.out.println(zdt);
    }
}
