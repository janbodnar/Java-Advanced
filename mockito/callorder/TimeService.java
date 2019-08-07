package com.zetcode.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeService {

    public LocalTime getTime() {

        return LocalTime.now();
    }

    public LocalDate getDate() {

        return LocalDate.now();
    }

    public LocalDateTime getDateTime() {

        return LocalDateTime.now();
    }
}
