package com.zetcode;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;

public class JavaDateCurrentEx {

    public static void main(String[] args) {

        LocalDate date = LocalDate.now();
        System.out.println("Current Date: " + date);

        LocalDate date2 = LocalDate.of(2017, Month.NOVEMBER, 13);
        System.out.println("Date from specified date: " + date2);

        // current year and month
        YearMonth yearMo = YearMonth.now();
        System.out.println("Current Year and month:" + yearMo);
        
        YearMonth specifiedDate = YearMonth.of(2000, Month.NOVEMBER);
        System.out.println("Specified Year-Month: " + specifiedDate);  
        
        MonthDay monthDay = MonthDay.now();
        System.out.println("Current month and day: " + monthDay);
        
        MonthDay specifiedDate2 = MonthDay.of(Month.NOVEMBER, 11);
        System.out.println("Specified Month-Day: " + specifiedDate2);        
        
    }

}
