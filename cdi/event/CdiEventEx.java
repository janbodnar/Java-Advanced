package com.zetcode;

import com.zetcode.service.TextService;
import com.zetcode.board.NotifyBoard;

import javax.enterprise.inject.se.SeContainerInitializer;

public class CdiEventEx {

    public static void main(String[] args) {

        var initializer = SeContainerInitializer.newInstance();

        try (var container = initializer.disableDiscovery()
                .addBeanClasses(TextService.class, NotifyBoard.class).initialize()) {

            var value = "old falcon";

            var textService = container.select(TextService.class).get();
            var modified = textService.upper(value);
            System.out.println(modified);
        }
    }
}
