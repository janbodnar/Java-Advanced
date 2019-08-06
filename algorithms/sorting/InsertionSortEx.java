package com.zetcode;

import java.util.Arrays;

/* Insertion sort is a simple sorting algorithm;
much less efficient on large lists than more advanced algorithms
such as quick sort, heap sort, or merge sort.
Efficient for (quite) small data sets */

// similar to sorting cards

public class InsertionSortEx {

    public static void main(String[] args) {

        int[] values = {3, 7, 1, 15, 12, 6, 11, 9};

        System.out.println(Arrays.toString(values));
        sort(values);
        System.out.println(Arrays.toString(values));
    }

    private static void sort(int a[]) {

        int len = a.length;

        // we start with the second element as key
        for (int i = 1; i < len; i++) {

            int key = a[i];
            int j = i - 1;

            // Move elements that are greater than key, to one
            // position ahead of their current position
            while (j >= 0 && a[j] > key) {

                a[j + 1] = a[j];
                j = j - 1;
            }

            a[j + 1] = key;
        }
    }
}
