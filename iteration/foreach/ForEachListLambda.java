package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class ForEachListLambda {

    public static void main(String[] args) {
        
        List<String> items = new ArrayList<>();

        items.add("coins");
        items.add("pens");
        items.add("keys");
        items.add("sheets");

        items.forEach((String name) -> {
            System.out.println(name);
        });
    }
}
