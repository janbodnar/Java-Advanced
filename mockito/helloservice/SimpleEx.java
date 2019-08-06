package com.zetcode;

import com.zetcode.service.HelloService;

public class SimpleEx {

    public static void main(String[] args) {

        var helloService = new HelloService();
        System.out.println(helloService.getMessage());
    }
}
