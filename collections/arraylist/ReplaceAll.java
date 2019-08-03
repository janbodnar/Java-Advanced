package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ReplaceAll {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();
        items.add("coin");
        items.add("pen");
        items.add("cup");
        items.add("notebook");
        items.add("class");

        UnaryOperator<String> uo = (x) -> x.toUpperCase();

        items.replaceAll(uo);

        System.out.println(items);
    }
}
