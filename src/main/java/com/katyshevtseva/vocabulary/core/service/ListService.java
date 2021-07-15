package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.AddingStatistics;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateCorrector.getProperDate;


public class ListService {
    private VocDao dao;

    public ListService(VocDao dao) {
        this.dao = dao;
    }

    public void addEntryToList(String word, String translation, int page, WordList list) {
        addStatistics();
        Entry entry = new Entry();
        entry.setWord(word);
        entry.setTranslation(translation);
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(getProperDate());
        entry.setCreationDate(new Date());
        entry.setPage(page);
        list.getEntries().add(entry);
        dao.saveNewEntry(entry);
    }

    public void addEntryToList(FrequentWord frequentWord, int page, WordList list) {
        addStatistics();
        Entry entry = new Entry();
        entry.setWord(frequentWord.getWord());
        entry.setTranslation(frequentWord.getTranslation());
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(getProperDate());
        entry.setCreationDate(new Date());
        entry.setPage(page);
        entry.setFrequentWord(frequentWord);
        list.getEntries().add(entry);
        dao.saveNewEntry(entry);
    }

    public void editEntry(Entry entry, String newWord, String newTranslation, int newPage) {
        entry.setWord(newWord);
        entry.setTranslation(newTranslation);
        entry.setPage(newPage);
        dao.saveEditedEntry(entry);
    }

    public void moveEntries(List<Entry> entries, WordList newWordList) {
        entries.forEach(entry -> {
            entry.getWordList().getEntries().remove(entry);
            entry.setWordList(newWordList);
            newWordList.getEntries().add(entry);
            dao.saveEditedEntry(entry);
        });
    }

    public void deleteEntries(List<Entry> entries) {
        entries.forEach(entry -> {
            entry.getWordList().getEntries().remove(entry);
            dao.deleteEntry(entry);
        });
    }

    private void addStatistics() {
        AddingStatistics statistics = dao.getAddingStatisticsOrNull(getProperDate());
        if (statistics == null) {
            statistics = new AddingStatistics();
            statistics.setDate(getProperDate());
            statistics.setNum(1);
            dao.saveNewAddingStatistics(statistics);
        } else {
            statistics.setNum(statistics.getNum() + 1);
            dao.saveEditedAddingStatistics(statistics);
        }
    }

    public AddingStatistics getTodayAddingStatisticsOrNull() {
        return dao.getAddingStatisticsOrNull(getProperDate());
    }

    public int getPageOfLastAddedWord(WordList wordList) {
        return dao.getPageOfLastAddedWord(wordList);
    }
}
