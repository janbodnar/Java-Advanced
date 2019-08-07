package com.zetcode;

import com.zetcode.service.TimeService;

public class CallOrder {

    public static void main(String[] args) {

        var timeService = new TimeService();

        System.out.println(timeService.getTime());
        System.out.println(timeService.getDate());
        System.out.println(timeService.getDateTime());
    }
}
