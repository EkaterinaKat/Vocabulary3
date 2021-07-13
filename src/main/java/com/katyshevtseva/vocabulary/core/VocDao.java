package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.entity.*;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;

import java.util.Date;
import java.util.List;

public interface VocDao {
    List<WordList> getAllWordLists();

    void saveNewWordList(WordList wordList);

    void saveEditedWordList(WordList wordList);

    List<Entry> getAllEntries();

    void saveNewEntry(Entry entry);

    void saveEditedEntry(Entry entry);

    LearningStatistics getLearningStatisticsOrNull(Date date, int level);

    void saveNewLearningStatistics(LearningStatistics learningStatistics);

    void saveEditedLearningStatistics(LearningStatistics learningStatistics);

    List<LearningStatistics> getStatistics(Date date);

    void deleteEntry(Entry entry);

    AddingStatistics getAddingStatisticsOrNull(Date date);

    void saveNewAddingStatistics(AddingStatistics addingStatistics);

    void saveEditedAddingStatistics(AddingStatistics addingStatistics);

    void saveLearningLog(LearningLog learningLog);

    int getPageOfLastAddedWord(WordList wordList);

    List<FrequentWord> getAllFrequentWords();

    void saveNewFrequentWord(FrequentWord frequentWord);

    void saveEditedFrequentWord(FrequentWord frequentWord);

    int countFrequentWordByStatus(Status status);

    List<FrequentWord> getFrequentWordsByStatus(Status status);
}
