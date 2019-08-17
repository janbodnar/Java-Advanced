package com.zetcode.web;

import com.zetcode.bean.Country;
import com.zetcode.service.CountryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ReadCountries", urlPatterns = {"/read"})
public class ReadCountries extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        Optional<List<Country>> countries = CountryService.getListOfCountries();

        String templateName;

        if (countries.isPresent()) {

            request.setAttribute("countries", countries.get());
            templateName = "listCountries.jsp";
        } else {

            templateName = "showError.jsp";
        }

        var dispatcher = request.getRequestDispatcher(templateName);
        dispatcher.forward(request, response);
    }
}
