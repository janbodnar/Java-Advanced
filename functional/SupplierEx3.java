package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SupplierEx3 {

    public static void main(String[] args) {

        // new operator itself is a supplier, of the reference to the newly created object
        Supplier<List<String>> listSupplier = ArrayList::new;

        List<String> words = fillList(listSupplier);
        System.out.println(words);

    }

    private static List<String> fillList(Supplier<List<String>> data) {

        List<String> words = data.get();
        words.add("falcon");
        words.add("wood");
        words.add("tree");
        words.add("forest");

        return words;
    }
}

