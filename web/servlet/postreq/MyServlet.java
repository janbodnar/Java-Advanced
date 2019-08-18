package com.zetcode.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyServlet", urlPatterns = {"/MyServlet"})
public class MyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String server = request.getServerName();
        int port = request.getServerPort();

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Request details</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>HTTP method: " + method + "</p>");
        out.println("<p>URL: " + url + "</p>");
        out.println("<p>Server: " + server + "</p>");
        out.println("<p>Port: " + port + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
}
