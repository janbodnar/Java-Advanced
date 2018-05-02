package com.zetcode;

import java.util.ArrayList;
import java.util.List;

public class NumbersEx {

    public static void main(String[] args) {
        
        List<Number> ls = new ArrayList<>();
        
        ls.add(1342341);
        ls.add(new Float(34.56));
        ls.add(235.242);
        ls.add(new Byte("102"));
        ls.add(new Short("1245"));
       
        for (Number n : ls) {
            
            System.out.println(n.getClass());
            System.out.println(n);
        }
    }
}
