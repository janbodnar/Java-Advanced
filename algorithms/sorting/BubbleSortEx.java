package com.zetcode;

import java.util.Arrays;

// Sorting an array of integers using bubble sort algorithm
public class BubbleSortEx {

    public static void main(String[] args) {

        int nums[] = {3, 7, 1, 15, 12, 6, 11, 9};
        
        doBubbleSort(nums);

        System.out.println(Arrays.toString(nums));
    }

    private static void doBubbleSort(int a[]) {

        int len = a.length;

        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - i - 1; j++) {

                if (a[j] > a[j + 1]) {

                    // swap elements
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }
}
