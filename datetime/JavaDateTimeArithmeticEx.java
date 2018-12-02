package com.zetcode;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

public class JavaDateTimeArithmeticEx {

    public static void main(String[] args) {

        var now = LocalDate.now();
        var date1 = now.plusDays(30);
        System.out.println(date1);

        var date2 = now.minusWeeks(15);
        System.out.println(date2);

        var date3 = now.plusYears(6);
        System.out.println(date3);

        var newYear = LocalDate.of(2018, 1, 1);
        long dif = DAYS.between(newYear, now);

        System.out.printf("Days passed since beginning of 2018: %d%n", dif);
    }
}
