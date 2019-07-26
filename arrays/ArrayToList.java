package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayToList {

    public static void main(String[] args) {

        int[] ints = {1, 2, 3, 4, 5, 6, 7};

        List<Integer> vals = Arrays.stream(ints).boxed().collect(Collectors.toList());

        System.out.println(vals);

        Integer[] nums = {2, 4, 6, 8, 10};
        List<Integer> vals2 = Arrays.asList(nums);
        List<Integer> vals3 = Arrays.stream(nums).collect(Collectors.toList());

        System.out.println(vals2);
        System.out.println(vals3);
    }
}
