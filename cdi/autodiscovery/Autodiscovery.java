package com.zetcode;

import com.zetcode.service.TextService;
import com.zetcode.service.TimeService;

import javax.enterprise.inject.se.SeContainerInitializer;

public class Autodiscovery {

    public static void main(String[] args) {

        var initializer = SeContainerInitializer.newInstance();

        try (var container = initializer.initialize()) {

            var value = "old falcon";

            var textService = container.select(TextService.class).get();
            var modified = textService.upper(value);
            System.out.println(modified);

            var timeService = container.select(TimeService.class).get();
            var now = timeService.getTime();

            System.out.println(now);
        }
    }
}
