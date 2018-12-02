package com.zetcode;

import java.util.Arrays;
import java.util.Random;

// Sorting an array of primitive integer values

public class ArraysSortEx {

    public static void main(String[] args) {

        var nums = new int[10];
        var rand = new Random();

        for (int i = 0; i < nums.length; i++) {
            nums[i] = rand.nextInt(100) + 1;
        }

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
