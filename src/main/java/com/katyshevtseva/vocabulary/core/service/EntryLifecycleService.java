package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.date.DateUtils.TimeUnit;
import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.util.Date;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.vocabulary.core.CoreConstants.LEVEL_DAYS_MAP;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;

public class EntryLifecycleService {

    static boolean entryIsRipe(Entry entry) {
        if (entry.getLevel() == MAX_LEVEL)
            return false;
        int daysNeedToPath = LEVEL_DAYS_MAP.get(entry.getLevel());
        Date appointmentDay = shiftDate(entry.getLastRepeat(), TimeUnit.DAY, daysNeedToPath);
        return !appointmentDay.after(new Date());
    }
}
