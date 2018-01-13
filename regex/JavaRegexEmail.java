package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Jane    the 'Jane' string
// ^Jane   'Jane' at the start of a string
// Jane$   'Jane' at the end of a string
// ^Jane$  exact match of the string 'Jane'
// [abc]   a, b, or c
// [a-z]   any lowercase letter
// [^A-Z]  any character that is not a uppercase letter
// (Jane|Becky)   Matches either 'Jane' or 'Becky'
// [a-z]+   one or more lowercase letters
// ^[98]?$  digits 9, 8 or empty string       
// ([wx])([yz])  wy, wz, xy, or xz
// [0-9]         any digit
// [^A-Za-z0-9]  any symbol (not a number or a letter)

public class JavaRegexEmail {

    public static void main(String[] args) {
        
        List<String> emails = Arrays.asList("luke@gmail.com", 
                "andy@yahoocom", "34234sdfa#2345", "f344@gmail.com");

        Pattern p = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}$");

        for (String email : emails) {

            Matcher m = p.matcher(email);

            if (m.matches()) {
                System.out.printf("%s matches%n", email);
            } else {
                System.out.printf("%s does not match%n", email);
            }
        }
    }
}
