package com.zetcode;

import java.util.Arrays;

public class SelectionSortEx {

    private static void doSelectionSort(int[] a) {

        int len = a.length;

        for (int i = 0; i < len - 1; i++) {

            int min_idx = i;

            for (int j = i + 1; j < len; j++) {
                if (a[j] < a[min_idx]) {
                    min_idx = j;
                }
            }

            int temp = a[min_idx];
            a[min_idx] = a[i];
            a[i] = temp;
        }
    }


    public static void main(String args[]) {

        int nums[] = {3, 7, 1, 15, 12, 6, 11, 9};

        doSelectionSort(nums);

        System.out.println(Arrays.toString(nums));
    }
}
