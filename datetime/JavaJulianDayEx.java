package com.zetcode;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import java.time.temporal.JulianFields;
import java.util.Date;

public class JavaJulianDayEx {

    public static void main(String[] args) {

        // Date cannot represent historical dates
        Date mdt = new Date(1812, 9, 7);
        System.out.println(mdt);

        LocalDate date = LocalDate.now();
        long todayJulianDay = date.getLong(JulianFields.JULIAN_DAY);

        System.out.println(todayJulianDay);

        LocalDate borodinoBattle = LocalDate.of(1812, 9, 7);
        System.out.println(borodinoBattle);

        long borodinoJulianDay = borodinoBattle.getLong(JulianFields.JULIAN_DAY);
        System.out.println(borodinoJulianDay);

        System.out.println(todayJulianDay - borodinoJulianDay);

        long daysBetween = DAYS.between(borodinoBattle, date);
        System.out.println(daysBetween);
    }
}
