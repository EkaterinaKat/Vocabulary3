package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.List;

import static com.katyshevtseva.vocabulary.core.DateCorrector.getProperDate;

public class ListService {
    private VocDao dao;

    public ListService(VocDao dao) {
        this.dao = dao;
    }

    public void addEntryToList(String word, String translation, WordList list) {
        Entry entry = new Entry();
        entry.setWord(word);
        entry.setTranslation(translation);
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(getProperDate());
        list.getEntries().add(entry);
        dao.saveNewEntry(entry);
    }

    public void editEntry(Entry entry, String newWord, String newTranslation) {
        entry.setWord(newWord);
        entry.setTranslation(newTranslation);
        dao.saveEditedEntry(entry);
    }

    public void moveEntries(List<Entry> entries, WordList newWordList) {
        for (Entry entry : entries) {
            entry.getWordList().getEntries().remove(entry);
            entry.setWordList(newWordList);
            newWordList.getEntries().add(entry);
            dao.saveEditedEntry(entry);
        }
    }

    public void deleteEntries(List<Entry> entries) {
        for (Entry entry : entries) {
            entry.getWordList().getEntries().remove(entry);
            dao.deleteEntry(entry);
        }
    }
}
