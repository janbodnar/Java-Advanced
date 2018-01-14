package com.zetcode;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class JavaInstantEx {

    public static void main(String[] args) {

        Instant timestamp = Instant.now();
        System.out.println("The current timestamp: " + timestamp);
        
        System.out.printf("Unix time: %d%n", timestamp.toEpochMilli());

        //Now minus five days
        Instant minusThree = timestamp.minus(5, ChronoUnit.DAYS);
        System.out.println("Now minus five days:" + minusThree);

        ZonedDateTime atZone = timestamp.atZone(ZoneId.of("GMT"));
        System.out.printf("GMT: %s%n", atZone);

        Instant yesterday = Instant.now().minus(24, ChronoUnit.HOURS);
        System.out.println("Yesterday: " + yesterday);
    }
}
