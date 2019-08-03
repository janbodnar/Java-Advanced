package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class ContainsElement {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();

        items.add("coin");
        items.add("pen");
        items.add("cup");
        items.add("notebook");
        items.add("class");

        var item = "pen";

        if (items.contains(item)) {

            System.out.printf("There is a %s in the list%n", item);
        }
    }
}
