package com.zetcode;

import java.util.function.Consumer;

// Consumer represents an operation that accepts a single input argument and 
// returns no result. Unlike most other functional interfaces, Consumer is expected
// to operate via side-effects.

public class ConsumerEx {

    public static void main(String[] args) {

        Consumer<String> showThreeTimes = value -> {

            System.out.println(value);
            System.out.println(value);
            System.out.println(value);
        };

        showThreeTimes.accept("Blue sky");
    }
}
