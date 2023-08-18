package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.Date;
import java.util.List;

public interface VocDao {

    <T> void saveNew(T t);

    <T> void saveEdited(T t);

    <T> void delete(T t);

    List<WordList> findListsByArchived(boolean archived);

    List<Entry> getAllEntries();

    List<Entry> getEntriesByCreationDate(Date date);

    List<LearningLog> getLearningLogsByDate(Date date);

    int getNumOfAddedEntriesByDate(Date date);

    List<LearningLog> getLearningLogsByEntry(Entry entry);

    int getPageOfLastAddedWord(WordList wordList);

    int countFrequentWordByStatus(Status status);

    List<FrequentWord> getFrequentWordsByStatus(Status status);

    List<FrequentWord> searchFrequentWord(String term);

    List<Entry> searchEntries(String term);

    int countWordsInNotArchivedListsByLevel(int level);
}
