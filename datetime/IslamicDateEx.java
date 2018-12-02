package com.zetcode;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;

public class IslamicDateEx {

    public static void main(String[] args) {

        var nowIslamic = HijrahDate.now();
        System.out.println("Islamic date: " + nowIslamic);

        var nowGregorian = LocalDate.now();
        System.out.println("Gregorian date: " + nowGregorian);
    }
}
