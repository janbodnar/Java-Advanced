package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class ModifyList {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();
        fillList(items);

        System.out.println(items);

        items.set(3, "watch");
        items.add("bowl");
        items.remove(0);
        items.remove("glass");

        System.out.println(items);

        items.removeIf(e -> e.charAt(0) == 'p');
        System.out.println(items);

        items.clear();

        if (items.isEmpty()) {

            System.out.println("The list is empty");
        } else {
            System.out.println("The list is not empty");
        }
    }

    private static void fillList(List<String> items) {

        items.add("coin");
        items.add("pen");
        items.add("pencil");
        items.add("clock");
        items.add("book");
        items.add("spectacles");
        items.add("glass");
    }
}
