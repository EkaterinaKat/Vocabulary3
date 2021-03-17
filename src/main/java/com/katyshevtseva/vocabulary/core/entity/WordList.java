package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class WordList implements Comparable<WordList> {
    private static int ID_COUNT = 1;
    private Long id;
    private String title;
    private List<Entry> entries = new ArrayList<>();

    public WordList() {
        id = (long) ID_COUNT;
        ID_COUNT++;
    }

    @Override
    public String toString() {
        return title;
    }

//    public List<Entry> getEntries() {
//        List<Entry> entries = new ArrayList<>();
//        Entry entry = new Entry();
//        entry.setWord("cat");
//        entry.setTranslation("кот");
//        entry.setLevel(5);
//        entry.setWordList(this);
//        entries.add(entry);
//        return entries;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordList wordList = (WordList) o;
        return id.equals(wordList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(WordList o) {
        return Long.compare(id, o.getId());
    }
}
