package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "word_list")
public class WordList implements Comparable<WordList> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean archived;

    @OneToMany(mappedBy = "wordList")
    private List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntriesSortedByDate() {
        return entries.stream()
                .sorted(Comparator.comparing(Entry::getLevel))
                .sorted(Comparator.comparing(Entry::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Entry> getEntriesSortedByLevel() {
        return entries.stream().sorted(Comparator.comparing(Entry::getLevel))
                .collect(Collectors.toList());
    }

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
