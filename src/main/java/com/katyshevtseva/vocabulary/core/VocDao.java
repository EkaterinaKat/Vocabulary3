package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.entity.AddingStatistics;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningStatistics;
import com.katyshevtseva.vocabulary.core.entity.WordList;

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
}
