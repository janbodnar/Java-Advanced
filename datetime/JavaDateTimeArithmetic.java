package com.zetcode;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

public class JavaDateTimeArithmetic {

    public static void main(String[] args) {
        
        LocalDate now = LocalDate.now();
        LocalDate ndate1 = now.plusDays(30);
        
        System.out.println(ndate1);
        
        LocalDate ndate2 = now.minusWeeks(15);
        System.out.println(ndate2);
        
        
        LocalDate newYear = LocalDate.of(2018, 1, 1);
        long dif = DAYS.between(newYear, now);
        System.out.printf("Days passed since beginning of 2018: %d%n", dif);
    }
}
