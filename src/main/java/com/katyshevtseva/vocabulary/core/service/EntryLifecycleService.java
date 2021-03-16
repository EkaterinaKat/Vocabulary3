package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.date.Utils;
import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntryLifecycleService {
    private final int MAX_LEVEL = 8;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static Map<Integer, Integer> levelDaysMap = new HashMap<>();

    static {
        levelDaysMap.put(1, 3);
        levelDaysMap.put(2, 4);
        levelDaysMap.put(3, 6);
        levelDaysMap.put(4, 8);
        levelDaysMap.put(5, 10);
        levelDaysMap.put(6, 12);
        levelDaysMap.put(7, 15);
    }

    public String getRipenessInfo(Entry entry) {
        return dateFormat.format(entry.getLastRepeat()) +
                String.format(" (%s)", entryIsRipe(entry));
    }

    public boolean entryIsRipe(Entry entry) {
        if (entry.getLevel() == MAX_LEVEL)
            return false;
        if (entry.getLevel() == 0)
            return true;
        int daysNeedToPath = levelDaysMap.get(entry.getLevel());
        Date appointmentDay = Utils.shiftDate(entry.getLastRepeat(), Utils.TimeUnit.DAY, daysNeedToPath);
        return !appointmentDay.after(new Date());
    }
}
