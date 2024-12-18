package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    // test11 and test12
    public void test1() {
        Date start = new Date();
        for (Entry entry : VocDao.getAllEntries()) {
            List<LearningLog> logs = VocDao.getLearningLogsByEntry(entry).stream()
                    .sorted(Comparator.comparing(LearningLog::getDate)).collect(Collectors.toList());
            if (logs.size() > 0)
                for (int i = 0; i < logs.size() - 1; i++) {
                    LearningLog log = logs.get(i);
                    LearningLog nextLog = logs.get(i + 1);
                    test11(log, nextLog);
                }
        }
        Date finish = new Date();
        System.out.println("finished. time=" + (finish.getTime() - start.getTime()));
    }

    // Проверка того что в случае положительного ответа уровень увеличивается на 1,
    // а в случае отрицательного обнуляется
    private void test11(LearningLog log, LearningLog nextLog) {
        if (log.isPositiveAnswer() && !(log.getInitLevel() + 1 == nextLog.getInitLevel())) {
            error();
        }
        if (!log.isPositiveAnswer() && nextLog.getInitLevel() != 0) {
            error();
        }
    }

    private void error() {
        System.out.println("Error");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Выводит логи по изучению всех слов которые были созданы заданное время назад
    public void test2() {
        //fullCycle = 59
        Date someDaysAgo = DateUtils.shiftDate(new Date(), DateUtils.TimeUnit.DAY, -64);
        List<Entry> entries = VocDao.getEntriesByCreationDate(someDaysAgo);
        for (Entry entry : entries) {
            System.out.println("\n" + entry.toString() + "\n");
            List<LearningLog> logs = VocDao.getLearningLogsByEntry(entry).stream()
                    .sorted(Comparator.comparing(LearningLog::getDate)).collect(Collectors.toList());
            for (LearningLog log : logs) {
                System.out.println(" * " + log.getStringForTest());
            }
            System.out.println("\n");
        }
    }
}
