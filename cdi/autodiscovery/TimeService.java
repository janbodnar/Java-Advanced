package com.zetcode.service;

import java.time.LocalTime;

public class TimeService {

    public String getTime() {

        return LocalTime.now().toString();
    }
}
