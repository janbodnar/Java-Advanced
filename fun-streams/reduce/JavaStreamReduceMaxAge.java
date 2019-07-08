package com.zetcode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JavaStreamReduceMaxAge {
    
    public static void main(String[] args) {
        
        List<MyUser> users = new ArrayList<>();
        users.add(new MyUser("Frank", LocalDate.of(1979, 11, 23)));
        users.add(new MyUser("Peter", LocalDate.of(1985, 1, 18)));
        users.add(new MyUser("Lucy", LocalDate.of(2002, 5, 14)));
        users.add(new MyUser("Albert", LocalDate.of(1996, 8, 30)));
        users.add(new MyUser("Frank", LocalDate.of(1967, 10, 6)));
        
        int maxAge = users.stream().mapToInt(MyUser::getAge)
                .reduce(0, (a1, a2) -> a1 > a2 ? a1 : a2);
        
        System.out.printf("The oldest user's age: %s%n", maxAge);
    }
}
