package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

class MyOperator<T> implements UnaryOperator<String> {

    @Override
    public String apply(String var) {

        if (var == null || var.length() == 0) {
            return var;
        }
        return var.substring(0, 1).toUpperCase() + var.substring(1);
    }
}

public class ReplaceAll2 {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();

        items.add("coin");
        items.add("pen");
        items.add("cup");
        items.add("notebook");
        items.add("glass");

        items.replaceAll(new MyOperator<>());

        System.out.println(items);
    }
}
