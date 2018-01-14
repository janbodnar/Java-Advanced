package com.zetcode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JavaParseDateTime {

    public static void main(String[] args) {

        String sd1 = "2018-01-04";
        LocalDate ld1 = LocalDate.parse(sd1);
        System.out.println("Date: " + ld1);

        String sdatetime = "2017-08-16T16:34:10";
        LocalDateTime ldt = LocalDateTime.parse(sdatetime);
        System.out.println("Datetime: " + ldt);

        DateTimeFormatter dtfmt = DateTimeFormatter.ofPattern("dd MMM uuuu");
        String anotherDate = "14 Aug 2017";
        LocalDate lds = LocalDate.parse(anotherDate, dtfmt);
        System.out.println(anotherDate + " parses to " + lds);
    }
}
