package com.zetcode;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;

public class JavaCustomTemporalAdjusterEx {

    public static void main(String[] args) {

        var localDate = LocalDate.of(2018, 12, 1);

        TemporalAdjuster taj = t -> t.plus(Period.ofDays(14));
        var result = localDate.with(taj);

        System.out.printf("Adding 14 days to %s gives %s", localDate, result);

    }
}
