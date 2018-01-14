package com.zetcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

public class JavaGetImage {

    private static HttpURLConnection con;

    public static void main(String[] args) throws IOException {

        String url = "http://httpbin.org/image/jpeg";

        try {
            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            BufferedImage image = ImageIO.read(con.getInputStream());
            ImageIO.write(image, "jpg", new File("src/resources/jackal.jpg"));

        } finally {

            con.disconnect();
        }
    }
}
