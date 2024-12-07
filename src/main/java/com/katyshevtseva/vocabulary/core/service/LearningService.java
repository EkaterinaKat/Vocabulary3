package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;
import static com.katyshevtseva.vocabulary.core.service.EntryLifecycleService.entryIsRipe;

public class LearningService {

    public static List<Entry> getEntriesToLearn() {
        List<Entry> entries = VocDao.getAllEntries().stream()
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

    public static void changeEntryLevelAndStatistics(Entry entry, boolean positiveAnswer) {
        saveLearningLog(entry, positiveAnswer);
        entry.setLastRepeat(new Date());

        if (positiveAnswer)
            entry.setLevel(entry.getLevel() + 1);
        else
            entry.setLevel(0);

        if (entry.getLevel() == MAX_LEVEL && entry.getFrequentWord() != null) {
            entry.getFrequentWord().setStatus(FrequentWord.Status.LEARNED);
            VocDao.saveEdited(entry.getFrequentWord());
        }

        VocDao.saveEdited(entry);
    }

    private static void saveLearningLog(Entry entry, boolean positiveAnswer) {
        LearningLog learningLog = new LearningLog();
        learningLog.setEntry(entry);
        learningLog.setDate(new Date());
        learningLog.setInitLevel(entry.getLevel());
        learningLog.setPositiveAnswer(positiveAnswer);
        VocDao.saveNew(learningLog);
    }

    public static String getStatisticsReport() {
        StringBuilder report = new StringBuilder("Statistics:\n");
        getTodayLearningStatistics().stream().sorted(Comparator.comparing(LearningStatistics::getLevel))
                .forEach(statistics -> report.append(String.format("%s: %s/%s \n",
                        statistics.getLevel(), statistics.getFalseNum(), statistics.getAllNum())));
        return report.toString();
    }

    private static List<LearningStatistics> getTodayLearningStatistics() {
        List<LearningLog> logs = VocDao.getLearningLogsByDate(new Date());
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
