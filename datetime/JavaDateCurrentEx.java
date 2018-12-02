package com.zetcode;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;

public class JavaDateCurrentEx {

    public static void main(String[] args) {

        var date = LocalDate.now();
        System.out.println("Current Date: " + date);

        var date2 = LocalDate.of(2017, Month.NOVEMBER, 13);
        System.out.println("Date from specified date: " + date2);

        // current year and month
        var yearMo = YearMonth.now();
        System.out.println("Current Year and month:" + yearMo);

        var specifiedDate = YearMonth.of(2000, Month.NOVEMBER);
        System.out.println("Specified Year-Month: " + specifiedDate);

        var monthDay = MonthDay.now();
        System.out.println("Current month and day: " + monthDay);

        var specifiedDate2 = MonthDay.of(Month.NOVEMBER, 11);
        System.out.println("Specified Month-Day: " + specifiedDate2);
    }
}
