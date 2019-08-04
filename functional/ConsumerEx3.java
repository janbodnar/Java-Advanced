package com.zetcode;

import java.util.List;
import java.util.function.Consumer;

public class ConsumerEx3 {

    public static void main(String[] args) {

        var words = List.of("falcon", "wood", "rock", "forest",
                "river", "water");

        words.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {

                System.out.println(s);
            }
        });
    }
}
