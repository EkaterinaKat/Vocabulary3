package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WordList {
    private String title;
    private List<Entry> entries;

    @Override
    public String toString() {
        return title;
    }

    public List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();
        Entry entry = new Entry();
        entry.setWord("cat");
        entry.setTranslation("кот");
        entry.setLevel(5);
        entry.setWordList(this);
        entries.add(entry);
        return entries;
    }
}
