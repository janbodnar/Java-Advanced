package com.zetcode;

import com.zetcode.sort.MySelectionSort;

public class Application {

    public static void main(String[] args) {

        int[] arr1 = {9, 3, 4, 12, 61, 17, 31, 8, 42, 11};
        int[] arr2 = MySelectionSort.doSort(arr1);

        for (int el : arr2) {

            System.out.printf("%d ", el);
        }

        System.out.println();
    }
}
