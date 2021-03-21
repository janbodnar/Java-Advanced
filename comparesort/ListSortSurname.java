package com.zetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

// sorting a list of full names by surname

public class ListSortSurname {

    public static void main(String[] args) {

        var names = Arrays.asList("John Doe", "Lucy Smith",
                "Benjamin Young", "Robert Brown", "Thomas Moore",
                "Linda Black", "Adam Smith", "Jane Smith");

        Function<String, String> fun = (String fullName) -> fullName.split("\s")[1];
        names.sort(Comparator.comparing(fun).reversed());

        System.out.println(names);
    }
}
