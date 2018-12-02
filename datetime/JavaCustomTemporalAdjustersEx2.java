package com.zetcode;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

class NextChristmas implements TemporalAdjuster {

    @Override
    public Temporal adjustInto(Temporal temporal) {

        return temporal.with(ChronoField.MONTH_OF_YEAR, 12).with(ChronoField.DAY_OF_MONTH, 25);

    }
}

public class JavaCustomTemporalAdjustersEx2 {

    public static void main(String[] args) {

        var now = LocalDate.now();
        System.out.println("Today: " + now);

        var date2 = now.with(new NextChristmas());
        System.out.println("Next XMas: " + date2);
    }
}
