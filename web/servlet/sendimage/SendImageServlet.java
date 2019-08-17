package com.zetcode;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// The method returns null if no file is found. The file name should be
// preceded by a forward slash ("/") which indicates the file is available
// in a folder relative to the current context root directory.

//https://stackoverflow.com/questions/1829784/should-i-close-the-servlet-outputstream

@WebServlet(name = "SendImageServlet", urlPatterns = {"/image"})
public class SendImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ServletContext sc = getServletContext();

        try (InputStream is = sc.getResourceAsStream("/images/sid.jpg")) {

            // it is the responsibility of the container to close output stream
            OutputStream os = response.getOutputStream();

            if (is == null) {

                response.setContentType("text/plain");
                os.write("Failed to send image".getBytes());
            } else {

                byte[] buffer = new byte[1024];
                int bytesRead;

                response.setContentType("image/png");

                while ((bytesRead = is.read(buffer)) != -1) {

                    os.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
