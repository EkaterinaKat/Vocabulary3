package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Entry {
    private String word;
    private String translation;
    private int level;
    private WordList wordList;
    private Date lastRepeat;

    @Override
    public String toString() {
        return "Entry{" +
                "word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
