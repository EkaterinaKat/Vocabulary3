package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningStatistics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.vocabulary.core.CoreConstants.CRITICAL_LEVEL;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;
import static com.katyshevtseva.vocabulary.core.service.EntryLifecycleService.entryIsRipe;

public class LearningService {
    private VocDao dao;

    public LearningService(VocDao dao) {
        this.dao = dao;
    }

    public List<Entry> getEntriesToLearn() {
        List<Entry> allEntries = dao.getAllEntries();
        List<Entry> entriesToLearn = new ArrayList<>();
        for (Entry entry : allEntries) {
            if (!entry.getWordList().isArchived() && entry.getLevel() < MAX_LEVEL && entryIsRipe(entry))
                entriesToLearn.add(entry);
        }
        entriesToLearn.sort(Comparator.comparing(Entry::getLevel).thenComparing(Entry::getWordList));
        return entriesToLearn;
    }

    public void changeEntryLevelAndStatistics(Entry entry, boolean positiveAnswer) {
        addStatistics(positiveAnswer, entry.getLevel());
        entry.setLastRepeat(new Date());

        if (positiveAnswer) {
            entry.setLevel(entry.getLevel() + 1);
        } else if (entry.getLevel() == 0 || entry.getLevel() >= CRITICAL_LEVEL) {
            entry.setLevel(0);
        } else {
            entry.setLevel(entry.getLevel() - 1);
        }

        dao.saveEditedEntry(entry);
    }

    private void addStatistics(boolean correctAnswer, int currentLevel) {
        LearningStatistics statistics = dao.getLearningStatisticsOrNull(new Date(), currentLevel);
        if (statistics == null) {
            statistics = new LearningStatistics();
            statistics.setLevel(currentLevel);
            statistics.setDate(new Date());
            statistics.setAllNum(1);
            statistics.setFalseNum(correctAnswer ? 0 : 1);
            dao.saveNewLearningStatistics(statistics);
        } else {
            statistics.setAllNum(statistics.getAllNum() + 1);
            if (!correctAnswer)
                statistics.setFalseNum(statistics.getFalseNum() + 1);
            dao.saveEditedLearningStatistics(statistics);
        }
    }

    public String getStatisticsReport() {
        List<LearningStatistics> statisticsList = dao.getStatistics(new Date());
        statisticsList.sort(Comparator.comparing(LearningStatistics::getLevel));

        String report = "Statistics:\n";
        for (LearningStatistics statistics : statisticsList) {
            report += String.format("%s: %s/%s \n", statistics.getLevel(), statistics.getFalseNum(), statistics.getAllNum());
        }
        return report;
    }
}
