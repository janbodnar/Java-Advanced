package com.zetcode.web;

import com.zetcode.bean.City;
import com.zetcode.service.CityService;
import com.zetcode.util.JsonConverter;

import javax.servlet.ServletOutputStream;
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
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        ServletOutputStream sos = response.getOutputStream();

        List<City> cities = new CityService().getCities();

        var converter = new JsonConverter();
        String output = converter.convertToJson(cities);

        sos.print(output);
    }
}
