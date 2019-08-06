package com.zetcode;

import java.util.HashSet;
import java.util.Set;

public class ForEachSet {

    public static void main(String[] args) {
        
        Set<String> brands = new HashSet<>();

        brands.add("Nike");
        brands.add("IBM");
        brands.add("Google");
        brands.add("Apple");

        brands.forEach((e) -> { System.out.println(e); });
    }
}
