package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

public class ListService {

    public static void addEntryToList(String word, String translation, int page, WordList list, String example) {
        Entry entry = new Entry();
        entry.setWord(word.trim());
        entry.setTranslation(translation.trim());
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(new Date());
        entry.setCreationDate(new Date());
        entry.setPage(page);
        entry.setExample(example);
        list.getEntries().add(entry);
        VocDao.saveNew(entry);
    }

    public static void addEntryToList(FrequentWord frequentWord, int page, WordList list, String example) {
        Entry entry = new Entry();
        entry.setWord(frequentWord.getWord());
        entry.setTranslation(frequentWord.getTranslation());
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(new Date());
        entry.setCreationDate(new Date());
        entry.setPage(page);
        entry.setFrequentWord(frequentWord);
        entry.setExample(example);
        frequentWord.setStatus(FrequentWord.Status.NEED_TO_LEARN);
        list.getEntries().add(entry);
        VocDao.saveNew(entry);
        VocDao.saveEdited(frequentWord);
    }

    public static void editEntry(Entry entry, String newWord, String newTranslation, int newPage, String example) {
        entry.setWord(newWord.trim());
        entry.setTranslation(newTranslation.trim());
        entry.setPage(newPage);
        entry.setExample(example);
        VocDao.saveEdited(entry);
    }

    public static void moveEntries(List<Entry> entries, WordList newWordList) {
        entries.forEach(entry -> {
            entry.getWordList().getEntries().remove(entry);
            entry.setWordList(newWordList);
            newWordList.getEntries().add(entry);
            VocDao.saveEdited(entry);
        });
    }

    public static void deleteEntries(List<Entry> entries) {
        entries.forEach(entry -> {
            entry.getWordList().getEntries().remove(entry);
            VocDao.delete(entry);
        });
    }

    public static int getNumOfEntriesAddedToday() {
        return VocDao.getNumOfAddedEntriesByDate(new Date());
    }

    public static int getPageOfLastAddedWord(WordList wordList) {
        return VocDao.getPageOfLastAddedWord(wordList);
    }
}
