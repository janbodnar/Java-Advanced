package com.zetcode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// Iterator brings the Iterator design pattern, which is a common behavioral 
// pattern used to access the elements of a collection object in sequential 
// manner without any need to know its underlying representation. 

public class JavaIteratorEx {

    public static void main(String[] args) {
        
        List<String> items = Arrays.asList("coin", "ball", "lamp", "spoon");
        
        Iterator it = items.iterator();
        
        while (it.hasNext()) {
            
            System.out.println(it.next());
        }
    }
}
