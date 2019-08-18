package com.zetcode.web;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "DoChart", urlPatterns = {"/showChart"})
public class DoChart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("image/png");

        OutputStream os = response.getOutputStream();

        JFreeChart chart = getChart();
        int width = 500;
        int height = 350;

        ChartUtils.writeChartAsPNG(os, chart, width, height);
    }

//    public JFreeChart getChart() {
//
//        var dataset = new DefaultPieDataset();
//
//        dataset.setValue("Croatia", 22);
//        dataset.setValue("Bohemia", 34);
//        dataset.setValue("Bulgaria", 18);
//        dataset.setValue("Spain", 5);
//        dataset.setValue("Others", 21);
//
//        JFreeChart chart = ChartFactory.createPieChart("Popular destinations",
//                dataset, true, false, false);
//
//        chart.setBorderVisible(false);
//
//        return chart;
//    }

    public JFreeChart getChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(46, "Gold medals", "USA");
        dataset.setValue(38, "Gold medals", "China");
        dataset.setValue(29, "Gold medals", "UK");
        dataset.setValue(22, "Gold medals", "Russia");
        dataset.setValue(13, "Gold medals", "South Korea");
        dataset.setValue(11, "Gold medals", "Germany");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Olympic gold medals in London",
                "",
                "Gold medals",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        return barChart;
    }
}
