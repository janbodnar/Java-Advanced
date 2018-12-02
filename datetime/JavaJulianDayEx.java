package com.zetcode;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import java.time.temporal.JulianFields;
import java.util.Date;

// Julian day is the continuous count of days since the beginning of the
// Julian Period and is used primarily by astronomers, and in software for
// easily calculating elapsed days between two events

// The Julian Day Number (JDN) is the integer assigned to a whole solar day
// in the Julian day count starting from noon Universal time, with Julian day
// number 0 assigned to the day starting at noon on Monday, January 1, 4713 BC.

public class JavaJulianDayEx {

    public static void main(String[] args) {

        // Date cannot represent historical dates
        var mdt = new Date(1812, 9, 7);
        System.out.println(mdt);

        var date = LocalDate.now();
        long todayJulianDay = date.getLong(JulianFields.JULIAN_DAY);

        System.out.println(todayJulianDay);

        var borodinoBattle = LocalDate.of(1812, 9, 7);
        System.out.println(borodinoBattle);

        long borodinoJulianDay = borodinoBattle.getLong(JulianFields.JULIAN_DAY);
        System.out.println(borodinoJulianDay);

        System.out.println(todayJulianDay - borodinoJulianDay);

        long daysBetween = DAYS.between(borodinoBattle, date);
        System.out.println(daysBetween);
    }
}
