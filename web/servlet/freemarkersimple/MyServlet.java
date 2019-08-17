package com.zetcode.web;

import com.zetcode.bean.Car;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyServlet", urlPatterns = {"/cars"})
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        var cars = List.of(new Car("Audi", 52642),
                new Car("Volvo", 29000), new Car("Skoda", 9000));

        request.setAttribute("cars", cars);
        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }
}
