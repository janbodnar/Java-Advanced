package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class ListOfLists {

    public static void main(String[] args) {

        List<Integer> l1 = new ArrayList<>();
        l1.add(1);
        l1.add(2);
        l1.add(3);

        List<Integer> l2 = new ArrayList<>();
        l2.add(4);
        l2.add(5);
        l2.add(6);

        List<Integer> l3 = new ArrayList<>();
        l3.add(7);
        l3.add(8);
        l3.add(9);

        List<List<Integer>> nums = new ArrayList<>();
        nums.add(l1);
        nums.add(l2);
        nums.add(l3);

        System.out.println(nums);

        for (List<Integer> list : nums) {

            for (Integer n : list) {

                System.out.printf("%d ", n);
            }

            System.out.println();
        }
    }
}
