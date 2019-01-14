package com.zetcode.utils;

import java.util.List;
import java.util.stream.Collectors;

public class MathUtils {

    public static Integer sum(List<Integer> vals) {

        var sum = vals.stream().reduce(Integer::sum);

        return sum.get();
    }

    public static List<Integer> positive(List<Integer> vals) {

        return vals.stream().filter(val -> val > 0).collect(Collectors.toList());

    }

    public static List<Integer> negative(List<Integer> vals) {

        return vals.stream().filter(val -> val < 0).collect(Collectors.toList());
    }
}
