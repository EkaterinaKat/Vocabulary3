package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.date.DateUtils.TimeUnit;
import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.vocabulary.core.CoreConstants.LEVEL_DAYS_MAP;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;

public class EntryLifecycleService {
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public String getRipenessInfo(Entry entry) {
        if (entry.getLastRepeat() == null)
            return "Дата последнего повторения не задана";
        return dateFormat.format(entry.getLastRepeat()) +
                String.format(" (%s)", entryIsRipe(entry));
    }

    static boolean entryIsRipe(Entry entry) {
        if (entry.getLevel() == MAX_LEVEL || entry.getLastRepeat() == null)
            return false;
        if (entry.getLevel() == 0)
            return true;
        int daysNeedToPath = LEVEL_DAYS_MAP.get(entry.getLevel());
        Date appointmentDay = shiftDate(entry.getLastRepeat(), TimeUnit.DAY, daysNeedToPath);
        return !appointmentDay.after(new Date());
    }
}
