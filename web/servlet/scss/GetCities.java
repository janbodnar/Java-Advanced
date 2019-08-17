package com.zetcode.web;

import com.zetcode.bean.City;
import com.zetcode.service.CityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetCities", urlPatterns = {"/cities"})
public class GetCities extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        List<City> cities = new CityService().findAll();

        request.setAttribute("cities", cities);
        request.getRequestDispatcher("showCities.jsp").forward(request, response);
    }
}
