package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.vocabulary.core.CoreConstants.LEVEL_DAYS_MAP;

@RequiredArgsConstructor
public class Test {
    public static final Map<Integer, Integer> LEGACY_LEVEL_DAYS_MAP = new HashMap<>();
    public static Date DATE_OF_LEVEL_DAYS_MAP_CHANGE;
    private final VocDao vocDao;

    static {
        try {
            DATE_OF_LEVEL_DAYS_MAP_CHANGE = (READABLE_DATE_FORMAT.parse("19.03.2022"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LEGACY_LEVEL_DAYS_MAP.put(0, 0);
        LEGACY_LEVEL_DAYS_MAP.put(1, 3);
        LEGACY_LEVEL_DAYS_MAP.put(2, 4);
        LEGACY_LEVEL_DAYS_MAP.put(3, 6);
        LEGACY_LEVEL_DAYS_MAP.put(4, 8);
        LEGACY_LEVEL_DAYS_MAP.put(5, 10);
        LEGACY_LEVEL_DAYS_MAP.put(6, 12);
        LEGACY_LEVEL_DAYS_MAP.put(7, 15);
    }

    void execute() {
        Date start = new Date();

        for (Entry entry : vocDao.getAllEntries()) {
            List<LearningLog> logs = vocDao.getLearningLogsByEntry(entry).stream()
                    .sorted(Comparator.comparing(LearningLog::getDate)).collect(Collectors.toList());

            // Диапазон значений initLevel: 0-7
            for (LearningLog log : logs) {
                if (log.getInitLevel() < 0 || log.getInitLevel() > 7) {
                    throw new RuntimeException();
                }
            }

            for (int i = 0; i < logs.size() - 2; i++) {
                LearningLog log = logs.get(i);
                LearningLog nextLog = logs.get(i + 1);

                // После лога с positiveAnswer = true следующий должен быть с уровнем на 1 больше
                if (log.isPositiveAnswer() && log.getInitLevel() + 1 != nextLog.getInitLevel()) {
                    throw new RuntimeException();
                }

                // После лога с positiveAnswer = false следующий должен быть с нулевым уровнем
                if (!log.isPositiveAnswer() && nextLog.getInitLevel() != 0) {
                    throw new RuntimeException();
                }

                // Разница между датами соседних логов должна соответствовать уровню
                Date minDateOfNextLog = shiftDate(log.getDate(), DateUtils.TimeUnit.DAY,
                        getDaysInterval(nextLog.getInitLevel(), nextLog.getDate()));
                if (before(nextLog.getDate(), minDateOfNextLog)) {
                    throw new RuntimeException();
                }
            }
        }

        Date end = new Date();
        System.out.println("END " + (end.getTime() - start.getTime()));
    }

    private int getDaysInterval(int levelOfEntryBeforeLearning, Date dateOfNextLog) {
        if (DateUtils.before(dateOfNextLog, DATE_OF_LEVEL_DAYS_MAP_CHANGE)) {
            return LEGACY_LEVEL_DAYS_MAP.get(levelOfEntryBeforeLearning);
        } else {
            return LEVEL_DAYS_MAP.get(levelOfEntryBeforeLearning);
        }
    }
}
