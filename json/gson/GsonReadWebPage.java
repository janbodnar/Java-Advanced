package com.zetcode;

import com.google.gson.Gson;
import java.io.IOException;
import org.jsoup.Jsoup;

class TimeData {
    
    private String time;
    private Long milliseconds_since_epoch;
    private String date;

    @Override
    public String toString() {
        return "TimeData{" + "time=" + time + ", milliseconds_since_epoch=" 
                + milliseconds_since_epoch + ", date=" + date + '}';
    }
}


public class GsonReadWebPage {

    public static void main(String[] args) throws IOException {
        
        String webPage = "http://time.jsontest.com.";

        String data = Jsoup.connect(webPage).ignoreContentType(true).execute().body();
        
        Gson gson = new Gson();
        TimeData td = gson.fromJson(data, TimeData.class);
        
        System.out.println(td);
    }
}
