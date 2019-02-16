package com.zetcode;

import com.google.common.base.Splitter;

// needs Guava dependency

public class SplitStringGuavaEx {

    public static void main(String[] args) {

        var input = " falcon, \t\tforest\t, \t\t, moderate, sky\n";

        var result = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .splitToList(input);

        result.forEach(System.out::println);
    }
}
