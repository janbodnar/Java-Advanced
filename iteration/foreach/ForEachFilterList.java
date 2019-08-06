package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class ForEachFilterList {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();
        
        items.add("coins");
        items.add("pens");
        items.add("keys");
        items.add("sheets");

        items.stream().filter(item -> (item.length() == 4)).forEach(System.out::println);
    }
}
