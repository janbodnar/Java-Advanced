package com.zetcode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaGetImage {

    private static HttpURLConnection con;

    public static void main(String[] args) throws IOException {

        var url = "http://httpbin.org/image/jpeg";

        try {
            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            BufferedImage image = ImageIO.read(con.getInputStream());
            ImageIO.write(image, "jpg", new File("src/resources/jackal.jpg"));

        } finally {

            con.disconnect();
        }
    }
}
