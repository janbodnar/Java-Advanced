package com.zetcode;

import java.util.ArrayList;
import java.util.List;

// The subList() method returns a view of the portion of this list
// between the specified fromIndex, inclusive, and  toIndex, exclusive.

public class SubList {

    public static void main(String[] args) {

        List<String> items = new ArrayList<>();

        items.add("coin");
        items.add("pen");
        items.add("cup");
        items.add("notebook");
        items.add("glass");
        items.add("chair");
        items.add("ball");
        items.add("bowl");

        List<String> items2 = items.subList(2, 5);

        System.out.println(items2);

        items2.set(0, "bottle");

        System.out.println(items2);
        System.out.println(items);
    }
}
