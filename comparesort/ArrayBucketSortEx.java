package com.zetcode;

import java.util.Arrays;

// Sorting positive integers with Bucket (non-comparison) sort algorithm

public class ArrayBucketSortEx {

    static int[] doBucketSort(int[] vals, int max) {

        // Bucket Sort
        int[] bucket = new int[max + 1];
        int[] sorted_vals = new int[vals.length];

        for (int i = 0; i < vals.length; i++) {
            bucket[vals[i]]++;
        }

        int outPos = 0;

        for (int i = 0; i < bucket.length; i++) {
            for (int j = 0; j < bucket[i]; j++) {
                sorted_vals[outPos++] = i;
            }
        }

        return sorted_vals;
    }

    static int max(int[] vals) {

        int max = 0;

        for (int i = 0; i < vals.length; i++) {
            if (vals[i] > max) {
                max = vals[i];
            }
        }

        return max;
    }

    public static void main(String args[]) {

        int[] rnums = {3, 2, 2, 1, 16, 7, 12, 1, 7, 0, 23};

        int maxValue = max(rnums);

        System.out.println("Original array: ");
        System.out.println(Arrays.toString(rnums));

        System.out.println("Sorted array: ");

        int[] sorted = doBucketSort(rnums, maxValue);
        System.out.println(Arrays.toString(sorted));
    }
}
