package com.zetcode;

@FunctionalInterface
interface MessageRenderer {  
    
    public void render(String msg);  
}  

public class JavaLambdaEx2 {

    public static void main(String[] args) {
        
        MessageRenderer consoleMessageRenderer = (msg)-> {  
            System.out.println(msg);  
        };  
        
        consoleMessageRenderer.render("Today is a sunny day");

    }
}
