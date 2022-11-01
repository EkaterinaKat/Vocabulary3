package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class ListService {
    private final VocDao dao;

    public void addEntryToList(String word, String translation, int page, WordList list) {
        Entry entry = new Entry();
        entry.setWord(word);
        entry.setTranslation(translation);
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(new Date());
        entry.setCreationDate(new Date());
        entry.setPage(page);
        list.getEntries().add(entry);
        dao.saveNew(entry);
    }

    public void addEntryToList(FrequentWord frequentWord, int page, WordList list) {
        Entry entry = new Entry();
        entry.setWord(frequentWord.getWord());
        entry.setTranslation(frequentWord.getTranslation());
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(new Date());
        entry.setCreationDate(new Date());
        entry.setPage(page);
        entry.setFrequentWord(frequentWord);
        frequentWord.setStatus(FrequentWord.Status.NEED_TO_LEARN);
        list.getEntries().add(entry);
        dao.saveNew(entry);
        dao.saveEdited(frequentWord);
    }

    public void editEntry(Entry entry, String newWord, String newTranslation, int newPage) {
        entry.setWord(newWord);
        entry.setTranslation(newTranslation);
        entry.setPage(newPage);
        dao.saveEdited(entry);
    }

    public void moveEntries(List<Entry> entries, WordList newWordList) {
        entries.forEach(entry -> {
            entry.getWordList().getEntries().remove(entry);
            entry.setWordList(newWordList);
            newWordList.getEntries().add(entry);
            dao.saveEdited(entry);
        });
    }

    public void deleteEntries(List<Entry> entries) {
        entries.forEach(entry -> {
            entry.getWordList().getEntries().remove(entry);
            dao.delete(entry);
        });
    }

    public int getNumOfEntriesAddedToday() {
        return dao.getNumOfAddedEntriesByDate(new Date());
    }

    public int getPageOfLastAddedWord(WordList wordList) {
        return dao.getPageOfLastAddedWord(wordList);
    }
}
