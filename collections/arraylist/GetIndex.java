package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class GetIndex {

    public static void main(String[] args) {

        List<String> colours = new ArrayList<>();

        colours.add(0, "blue");
        colours.add(1, "orange");
        colours.add(2, "red");
        colours.add(3, "green");
        colours.add(4, "orange");

        int idx1 = colours.indexOf("orange");
        System.out.println(idx1);

        int idx2 = colours.lastIndexOf("orange");
        System.out.println(idx2);
    }
}
