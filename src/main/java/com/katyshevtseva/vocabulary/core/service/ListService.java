package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.Date;

public class ListService {

    public void addEntryToList(String word, String translation, WordList list) {
        Entry entry = new Entry();
        entry.setWord(word);
        entry.setTranslation(translation);
        entry.setLevel(0);
        entry.setWordList(list);
        entry.setLastRepeat(new Date());
        list.getEntries().add(entry);
    }
}
