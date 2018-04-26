package com.zetcode;

import java.util.ArrayList;
import java.util.List;

abstract class Shape {

    abstract void draw();
}

class Rectangle extends Shape {

    @Override
    void draw() {
        System.out.println("Drawing rectangle");
    }
}

class Circle extends Shape {

    @Override
    void draw() {
        System.out.println("Drawing circle");
    }
}

// Generic wildcard example

public class GenericWildcardEx {

    public static void main(String[] args) {
        
        List<Rectangle> list1 = new ArrayList<>();
        list1.add(new Rectangle());

        List<Circle> list2 = new ArrayList<>();
        list2.add(new Circle());
        list2.add(new Circle());

        drawShapes(list1);
        drawShapes(list2);
    }

    private static void drawShapes(List<? extends Shape> lists) {
        
        for (Shape s : lists) {
            s.draw();
        }
    }
}
