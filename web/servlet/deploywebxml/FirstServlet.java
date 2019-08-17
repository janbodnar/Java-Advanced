package com.zetcode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirstServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        var writer = response.getWriter();

        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>FirstServlet</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<p> My first servlet</p>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
