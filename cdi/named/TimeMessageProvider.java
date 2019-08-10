package com.zetcode.provider;

import javax.inject.Named;
import java.time.LocalTime;

@Named("TimeMessageProvider")
public class TimeMessageProvider implements MessageProvider {

    @Override
    public String getMessage() {

        return LocalTime.now().toString();
    }
}
