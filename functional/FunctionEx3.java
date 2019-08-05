package com.zetcode;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionEx3 {

    public static void main(String[] args) {

        var values = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
        }};

        var squared = values.stream().map(new Function<Integer, Object>() {
            @Override
            public Object apply(Integer integer) {
                return integer * integer;
            }
        }).collect(Collectors.toList());

        System.out.println(squared);
    }
}
