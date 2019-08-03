package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class AddAll {

    public static void main(String[] args) {

        List<String> colours1 = new ArrayList<>();

        colours1.add("blue");
        colours1.add("red");
        colours1.add("green");

        List<String> colours2 = new ArrayList<>();

        colours2.add("yellow");
        colours2.add("pink");
        colours2.add("brown");

        List<String> colours3 = new ArrayList<>();
        colours3.add("white");
        colours3.add("orange");

        colours3.addAll(colours1);
        colours3.addAll(2, colours2);

        System.out.println(colours3);
    }
}
