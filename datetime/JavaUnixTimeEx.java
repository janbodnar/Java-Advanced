package com.zetcode;

import java.time.Instant;

// Unix time (also known as POSIX time or epoch time)
// is a system for describing a point in time, defined as the number
// of seconds that have elapsed since 00:00:00 Coordinated Universal Time (UTC),
// Thursday, 1 January 1970, minus the number of leap seconds
// that have taken place since then

public class JavaUnixTimeEx {

    public static void main(String[] args) {

        long unixTime = System.currentTimeMillis() / 1000L;
        System.out.println(unixTime);

        // Java 8

        long unixTime2 = Instant.now().getEpochSecond();
        System.out.println(unixTime2);
    }
}
