package com.zetcode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

// Temporal adjusters are used for modifying temporal objects (LodalDate, LodalDateTime, HijrahDate )
// They allow to easily calculate calculations such as finding first day of week, moth, year etc.

public class JavaTemporalAdjustersEx {

    public static void main(String[] args) {

        var localDate = LocalDate.now();
        System.out.printf("today: %s%n", localDate);

        var date1 = localDate.with(TemporalAdjusters.firstDayOfMonth());
        System.out.printf("first day of month: %s%n", date1);

        var date2 = localDate.with(TemporalAdjusters.lastDayOfMonth());
        System.out.printf("last day of month: %s%n", date2);

        var date3 = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.printf("next Monday: %s%n", date3);

        var date4 = localDate.with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.printf("first day of next month: %s%n", date4);

        var date5 = localDate.with(TemporalAdjusters.lastDayOfYear());
        System.out.printf("last day of year: %s%n", date5);

        var date6 = localDate.with(TemporalAdjusters.firstDayOfYear());
        System.out.printf("first day of year: %s%n", date6);

        var date7 = localDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
        System.out.printf("last Sunday of month: %s%n", date7);
    }
}
