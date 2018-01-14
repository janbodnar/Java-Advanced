package com.zetcode.web;

import com.zetcode.service.WebPageReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ReadWebPage", urlPatterns = {"/ReadWebPage"})
public class ReadWebpage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        
        String page = request.getParameter("webpage");
        
        String content = new WebPageReader().setWebPageName(page).getWebPageContent();
                
        ServletOutputStream os = response.getOutputStream();
        os.write(content.getBytes(StandardCharsets.UTF_8));
    }
}
