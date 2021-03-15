package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public List<Entry> search(String string) {
        List<Entry> entries = new ArrayList<>();
        Entry entry = new Entry();
        entry.setWord("cat");
        entry.setTranslation("кот");
        entry.setLevel(5);
        WordList wordList = new WordList();
        wordList.setTitle("my list");
        entry.setWordList(wordList);
        entries.add(entry);
        return entries;
    }
}
