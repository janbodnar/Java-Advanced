package com.zetcode.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TextServlet", urlPatterns = {"/MyServlet"})
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("Dnes je pondelok, 12. Septembra 2016.");
        out.println("Vo štvrtok pôjdeme do mesta.");
        out.println(getServletContext().getServerInfo());

        out.println(request.getRequestURL());

        getServletContext().log("Today is monday");

    }
}
