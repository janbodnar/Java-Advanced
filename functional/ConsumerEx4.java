package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerEx4 {

    public static void main(String[] args) {

        var vals = new ArrayList<Integer>();
        vals.add(2);
        vals.add(4);
        vals.add(6);
        vals.add(8);

        Consumer<List<Integer>> addTwo = list -> {

            for (int i = 0; i < list.size(); i++) {
                list.set(i, 2 + list.get(i));
            }
        };

        Consumer<List<Integer>> showList = list -> list.forEach(System.out::println);

        addTwo.andThen(showList).accept(vals);
    }
}
