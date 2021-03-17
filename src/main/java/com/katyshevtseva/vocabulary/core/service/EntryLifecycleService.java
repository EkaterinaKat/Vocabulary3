package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.date.Utils;
import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.katyshevtseva.vocabulary.core.CoreConstants.LEVEL_DAYS_MAP;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;

public class EntryLifecycleService {
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public String getRipenessInfo(Entry entry) {
        return dateFormat.format(entry.getLastRepeat()) +
                String.format(" (%s)", entryIsRipe(entry));
    }

    static boolean entryIsRipe(Entry entry) {
        if (entry.getLevel() == MAX_LEVEL)
            return false;
        if (entry.getLevel() == 0)
            return true;
        int daysNeedToPath = LEVEL_DAYS_MAP.get(entry.getLevel());
        Date appointmentDay = Utils.shiftDate(entry.getLastRepeat(), Utils.TimeUnit.DAY, daysNeedToPath);
        return !appointmentDay.after(new Date());
    }
}
