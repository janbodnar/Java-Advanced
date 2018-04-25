package com.zetcode;

import java.util.Arrays;
import java.util.Comparator;

// Sorting an array of wrapper integer values

public class ArraysSortEx2 {

    public static void main(String[] args) {
        
        Integer[] vals = { 3, 2, 12, 5, 7, 12, 87, 43, 21, 12, 4 };
        
        Arrays.sort(vals, Comparator.naturalOrder());
        System.out.println(Arrays.toString(vals));
        
        Arrays.sort(vals, Comparator.reverseOrder());
        System.out.println(Arrays.toString(vals));
    }
}
