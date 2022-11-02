package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

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
        saveLearningLog(entry, positiveAnswer);
        entry.setLastRepeat(new Date());

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
        learningLog.setDate(new Date());
        learningLog.setInitLevel(entry.getLevel());
        learningLog.setPositiveAnswer(positiveAnswer);
        dao.saveNew(learningLog);
    }

    public String getStatisticsReport() {
        StringBuilder report = new StringBuilder("Statistics:\n");
        getTodayLearningStatistics().stream().sorted(Comparator.comparing(LearningStatistics::getLevel))
                .forEach(statistics -> report.append(String.format("%s: %s/%s \n",
                        statistics.getLevel(), statistics.getFalseNum(), statistics.getAllNum())));
        return report.toString();
    }

    private List<LearningStatistics> getTodayLearningStatistics() {
        List<LearningLog> logs = dao.getLearningLogsByDate(new Date());
        Map<Integer, List<LearningLog>> levelLogListMap = new HashMap<>();
        for (LearningLog log : logs) {
            List<LearningLog> list = levelLogListMap.computeIfAbsent(log.getInitLevel(), k -> new ArrayList<>());
            list.add(log);
        }
        List<LearningStatistics> statisticsList = new ArrayList<>();
        for (Map.Entry<Integer, List<LearningLog>> mapEntry : levelLogListMap.entrySet()) {
            LearningStatistics statistics = new LearningStatistics();
            statistics.setLevel(mapEntry.getKey());
            statistics.setAllNum(mapEntry.getValue().size());
            statistics.setFalseNum((int) mapEntry.getValue().stream().filter(log -> !log.isPositiveAnswer()).count());
            statisticsList.add(statistics);
        }
        return statisticsList;
    }

    @Data
    public static class LearningStatistics {
        private int level;
        private int allNum;
        private int falseNum;
    }
}
