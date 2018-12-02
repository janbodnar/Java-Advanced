package com.zetcode;

import java.util.Calendar;
import java.util.GregorianCalendar;

// Old API

public class JavaGregorianCalendarEx {

    public static void main(String[] args) {

        String months[] = {"Jan", "Feb", "Mar", "Apr", "May",
                "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        // Create a Gregorian calendar initialized
        // with the current date and time in the
        // default locale and timezone.

        var gcal = new GregorianCalendar();

        // Display current time and date information.
        System.out.printf("Date: %s %d %d%n", months[gcal.get(Calendar.MONTH)],
                gcal.get(Calendar.DATE), gcal.get(Calendar.YEAR));

        System.out.printf("Time: %d:%d:%d%n",gcal.get(Calendar.HOUR),
                gcal.get(Calendar.MINUTE),
                gcal.get(Calendar.SECOND));

        if (gcal.isLeapYear(gcal.get(Calendar.YEAR))) {
            System.out.println("The current year is a leap year");
        } else {
            System.out.println("The current year is not a leap year");
        }
    }
}
