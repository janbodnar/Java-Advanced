package com.zetcode.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        boolean authenticated = request.authenticate(response);

        if (authenticated) {
            if (request.getUserPrincipal() != null) {
                writer.printf("user %s authenticated",
                        request.getUserPrincipal().getName());
            }
        } else {
            return;
        }

        writer.println("; admin page - GET request");
    }
 }
