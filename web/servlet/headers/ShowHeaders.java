package com.zetcode.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "ShowHeaders", urlPatterns = {"/ShowHeaders"})
public class ShowHeaders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        PrintWriter out = response.getWriter();

        response.setContentType("text/html");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>HTTP headers</title>");
        out.println("<body>");
        out.println("<h2>Headers</h2>");

        Enumeration<String> headerNames = request.getHeaderNames();

        out.println("<ol>");

        while (headerNames.hasMoreElements()) {

            out.print("<li>");
            String headerName = headerNames.nextElement();
            out.print(headerName + " = ");

            String headerValue = request.getHeader(headerName);
            out.print(headerValue);
            out.println("</li>");
        }

        out.println("</ol>");

        out.println("</body>");
        out.println("</html>");
    }
}
