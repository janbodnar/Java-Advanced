package com.zetcode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JavaIteratorEx {

    public static void main(String[] args) {
        
        List<String> items = Arrays.asList("coin", "ball", "lamp", "spoon");
        
        Iterator it = items.iterator();
        
        while (it.hasNext()) {
            
            System.out.println(it.next());
        }
    }
}
