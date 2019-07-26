package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;

public class DownloadImageChannel {

    public static void main(String[] args) throws IOException {

        var urlFile = "http://httpbin.org/image/jpeg";

        var url = new URL(urlFile);

        try (var rbc = Channels.newChannel(url.openStream());
             var fos = new FileOutputStream("jackal.jpg");) {

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }
}
