package com.zetcode.provider;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeMessageProvider implements MessageProvider {

    private String format;

    @Override
    public String getMessage() {

        var dtf = DateTimeFormatter.ofPattern(format);
        return LocalTime.now().format(dtf);
    }

    public void setFormat(String format) {

        this.format = format;
    }
}
