package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ForEachConsumer {

    public static void main(String[] args) {
        
        List<String> items = new ArrayList<>();

        items.add("coins");
        items.add("pens");
        items.add("keys");
        items.add("sheets");

        items.forEach(new Consumer<String>() {
            @Override
            public void accept(String name) {
                System.out.println(name);
            }
        });
    }
}
