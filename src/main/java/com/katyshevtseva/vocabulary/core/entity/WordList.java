package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class WordList implements Comparable<WordList> {
    private Long id;
    private String title;
    private boolean archived;
    private List<Entry> entries = new ArrayList<>();

    @Override
    public String toString() {
        return title;
    }

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
