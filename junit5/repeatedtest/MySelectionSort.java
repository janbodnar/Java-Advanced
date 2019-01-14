package com.zetcode.sort;

public class MySelectionSort {

    public static int[] doSort(int[] arr){

        for (int i = 0; i < arr.length; i++)
        {
            int idx = i;

            for (int j = i + 1; j < arr.length; j++) {

                if (arr[j] < arr[idx]) {
                    idx = j;
                }
            }

            int smallerNumber = arr[idx];

            arr[idx] = arr[i];
            arr[i] = smallerNumber;
        }

        return arr;
    }
}
