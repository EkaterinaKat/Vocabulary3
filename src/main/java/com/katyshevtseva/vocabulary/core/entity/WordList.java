package com.katyshevtseva.vocabulary.core.entity;

import lombok.Data;

@Data
public class WordList {
    private String title;

    @Override
    public String toString() {
        return title;
    }
}
