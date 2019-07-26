package com.zetcode;

import java.util.Arrays;

// Sorting an array of primitive integer values

public class ArraySortPrimitive {

    public static void main(String[] args) {

        var nums = new int[]{3, 2, 8, 1, 7, 0, 5, 4, 6};

        Arrays.sort(nums);

        System.out.println(Arrays.toString(nums));

        for (int start = 0, end = nums.length - 1; start <= end; start++, end--) {

            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
        }

        System.out.println(Arrays.toString(nums));
    }
}
