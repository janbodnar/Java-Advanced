package com.zetcode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;

public class DownloadFileEx {

    public static void main(String[] args) throws IOException {

        var remoteFileName = "http://webcode.me/favicon.ico";
        var localFileName = "favicon.ico";

        var myUrl = new URL(remoteFileName);

        try (var rbc = Channels.newChannel(myUrl.openStream())) {
            try (var fos = new FileOutputStream(localFileName)) {

                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        }
    }
}
