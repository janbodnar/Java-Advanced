package com.zetcode;

import com.zetcode.com.zetcode.bag.ColorBag;

public class Application {

    public static void main(String[] args) {

        var colorBag = new ColorBag();

        colorBag.add("red");
        colorBag.add("green");
        colorBag.add("yellow");
        colorBag.add("blue");
        colorBag.add("magenta");
        colorBag.add("brown");

        System.out.printf("The size of the color bag is: %d%n", colorBag.size());

        colorBag.remove("magenta");

        System.out.printf("The size of the color bag is: %d%n", colorBag.size());

        if (colorBag.contains("magenta")) {

            System.out.println("Color bag contains magenta color");
        } else {

            System.out.println("Color bag does not contain magenta color");
        }

    }
}
