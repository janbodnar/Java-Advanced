package com.zetcode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Regular expression - defines a search pattern for strings
// Pattern - compiled representation of a regular expression
// Matcher - engine that interprets the pattern and performs 
//           match operations against an input string. 

public class JavaRegexEx {

    public static void main(String[] args) {

        List<String> words = Arrays.asList("Seven", "even",
                "Maven", "Amen", "eleven");

        Pattern p = Pattern.compile(".even");
        
        for (String word: words) {
            
            Matcher m = p.matcher(word);  
            
            if (m.matches()) {
                System.out.printf("%s matches%n", word);
            } else {
                System.out.printf("%s does not match%n", word);
            }
        }
    }
}
