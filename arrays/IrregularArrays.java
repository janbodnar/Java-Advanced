package com.zetcode;

public class IrregularArrays {

    public static void main(String[] args) {

        int[][] ir = new int[][] {
                {1, 2},
                {1, 2, 3},
                {1, 2, 3, 4}
        };

        for (int[] a : ir) {
            for (int e : a) {
                System.out.print(e + " ");
            }
        }

        System.out.print('\n');
    }
}
