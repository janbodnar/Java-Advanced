package com.zetcode;

import java.util.Arrays;

public class ForEachArray {

    public static void main(String[] args) {

        int[] nums = { 3, 4, 2, 1, 6, 7 };

        Arrays.stream(nums).forEach(System.out::println);
    }
}
