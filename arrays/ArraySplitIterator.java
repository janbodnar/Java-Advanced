package com.zetcode;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.IntConsumer;

public class ArraySplitIterator {

    // split iterator gives read-only access to a range 
    // of elements
    public static void main(String[] args) {

        int[] vals = {1, 2, 3, 4, 5, 6, 7, 8};

        Spliterator.OfInt sit = Arrays.spliterator(vals, 2, 6);

        sit.forEachRemaining((IntConsumer) System.out::println);
    }
}
