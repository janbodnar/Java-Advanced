package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

// Compare lists with Apache Commons library

public class CompareListsApacheCollsEx {
    
    public static void main(String[] args) {
        
        List<String> one = new ArrayList<>();
        List<String> two = new ArrayList<>();

        one.add("dog");
        one.add("pen");
        one.add("sky");
        one.add("rock");
        
        two.add("dog");
        two.add("pen");
        two.add("sky");
        two.add("rock");

        boolean equal = CollectionUtils.isEqualCollection(one, two);
        
        if (equal) {
            System.out.println("The lists are equal");
        } else {
            System.out.println("The lists are not equal");
        }        
        
    }
}
