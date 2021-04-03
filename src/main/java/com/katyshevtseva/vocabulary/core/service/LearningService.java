package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningStatistics;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateCorrector.getProperDate;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;
import static com.katyshevtseva.vocabulary.core.service.EntryLifecycleService.entryIsRipe;

public class LearningService {
    private VocDao dao;

    public LearningService(VocDao dao) {
        this.dao = dao;
    }

    public List<Entry> getEntriesToLearn() {
        return dao.getAllEntries().stream().filter(
                entry -> (!entry.getWordList().isArchived() && entry.getLevel() < MAX_LEVEL && entryIsRipe(entry)))
                .sorted(Comparator.comparing(Entry::getLevel).thenComparing(Entry::getWordList).thenComparing(Entry::getWord))
                .collect(Collectors.toList());
    }

    public void changeEntryLevelAndStatistics(Entry entry, boolean positiveAnswer) {
        addStatistics(positiveAnswer, entry.getLevel());
        entry.setLastRepeat(getProperDate());

        if (positiveAnswer)
            entry.setLevel(entry.getLevel() + 1);
        else
            entry.setLevel(0);

        dao.saveEditedEntry(entry);
    }

    private void addStatistics(boolean correctAnswer, int currentLevel) {
        LearningStatistics statistics = dao.getLearningStatisticsOrNull(getProperDate(), currentLevel);
        if (statistics == null) {
            statistics = new LearningStatistics();
            statistics.setLevel(currentLevel);
            statistics.setDate(getProperDate());
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
        StringBuilder report = new StringBuilder("Statistics:\n");
        dao.getStatistics(getProperDate()).stream().sorted(Comparator.comparing(LearningStatistics::getLevel))
                .forEach(statistics -> report.append(String.format("%s: %s/%s \n",
                        statistics.getLevel(), statistics.getFalseNum(), statistics.getAllNum())));
        return report.toString();
    }
}
