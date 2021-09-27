package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.entity.*;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;

import java.util.Date;
import java.util.List;

public interface VocDao {

    <T> void saveNew(T t);

    <T> void saveEdited(T t);

    <T> void delete(T t);

    List<WordList> getAllWordLists();

    List<Entry> getAllEntries();

    LearningStatistics getLearningStatisticsOrNull(Date date, int level);

    List<LearningStatistics> getStatistics(Date date);

    AddingStatistics getAddingStatisticsOrNull(Date date);

    List<LearningLog> getAllLearningLogs();

    int getPageOfLastAddedWord(WordList wordList);

    List<FrequentWord> getAllFrequentWords();

    int countFrequentWordByStatus(Status status);

    List<FrequentWord> getFrequentWordsByStatus(Status status);

    List<FrequentWord> searchFrequentWord(String term);

    List<Entry> searchEntries(String term);
}
