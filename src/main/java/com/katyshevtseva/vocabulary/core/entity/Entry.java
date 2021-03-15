package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

@Data
public class Entry {
    private String word;
    private String translation;
    private int level;
    private WordList wordList;
}
