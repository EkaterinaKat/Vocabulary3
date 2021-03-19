package com.katyshevtseva.vocabulary.core;

import java.time.LocalDateTime;
import java.util.Date;

import static com.katyshevtseva.date.Utils.TimeUnit.DAY;
import static com.katyshevtseva.date.Utils.shiftDate;

public class DateCorrector {

    public static Date getProperDate() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (hour > 6)
            return new Date();
        return shiftDate(new Date(), DAY, -1);
    }
}
