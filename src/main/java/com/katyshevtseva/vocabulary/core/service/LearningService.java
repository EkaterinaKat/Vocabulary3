package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.core.entity.LearningStatistics;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateCorrector.getProperDate;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;
import static com.katyshevtseva.vocabulary.core.service.EntryLifecycleService.entryIsRipe;

@RequiredArgsConstructor
public class LearningService {
    private final VocDao dao;

    public List<Entry> getEntriesToLearn() {
        List<Entry> entries = dao.getAllEntries().stream()
                .filter(entry -> (!entry.getWordList().isArchived() && entry.getLevel() < MAX_LEVEL && entryIsRipe(entry)))
                .collect(Collectors.toList());

        List<Entry> youngerEntries = entries.stream()
                .filter(entry -> entry.getLevel() < CoreConstants.CRITICAL_LEVEL)
                .sorted(Comparator.comparing(Entry::getLevel).thenComparing(Entry::getWordList).thenComparing(Entry::getCreationDate))
                .collect(Collectors.toList());

        List<Entry> olderEntries = entries.stream()
                .filter(entry -> entry.getLevel() >= CoreConstants.CRITICAL_LEVEL)
                .collect(Collectors.toList());
        Collections.shuffle(olderEntries);
        olderEntries = olderEntries.stream().sorted(Comparator.comparing(Entry::getLevel)).collect(Collectors.toList());

        List<Entry> result = new ArrayList<>();
        result.addAll(youngerEntries);
        result.addAll(olderEntries);

        return result;
    }

    public void changeEntryLevelAndStatistics(Entry entry, boolean positiveAnswer) {
        addStatistics(positiveAnswer, entry.getLevel());
        saveLearningLog(entry, positiveAnswer);
        entry.setLastRepeat(getProperDate());

        if (positiveAnswer)
            entry.setLevel(entry.getLevel() + 1);
        else
            entry.setLevel(0);

        if (entry.getLevel() == MAX_LEVEL && entry.getFrequentWord() != null) {
            entry.getFrequentWord().setStatus(FrequentWord.Status.LEARNED);
            dao.saveEdited(entry.getFrequentWord());
        }

        dao.saveEdited(entry);
    }

    private void saveLearningLog(Entry entry, boolean positiveAnswer) {
        LearningLog learningLog = new LearningLog();
        learningLog.setEntry(entry);
        learningLog.setDate(getProperDate());
        learningLog.setInitLevel(entry.getLevel());
        learningLog.setPositiveAnswer(positiveAnswer);
        dao.saveNew(learningLog);
    }

    private void addStatistics(boolean correctAnswer, int currentLevel) {
        LearningStatistics statistics = dao.getLearningStatisticsOrNull(getProperDate(), currentLevel);
        if (statistics == null) {
            statistics = new LearningStatistics();
            statistics.setLevel(currentLevel);
            statistics.setDate(getProperDate());
            statistics.setAllNum(1);
            statistics.setFalseNum(correctAnswer ? 0 : 1);
            dao.saveNew(statistics);
        } else {
            statistics.setAllNum(statistics.getAllNum() + 1);
            if (!correctAnswer)
                statistics.setFalseNum(statistics.getFalseNum() + 1);
            dao.saveEdited(statistics);
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
