package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.Arrays;
import java.util.List;

public class CatalogueService {

    public List<WordList> getCatalogue() {
        WordList wordList = new WordList();
        wordList.setTitle("list title1");
        WordList wordList1 = new WordList();
        wordList1.setTitle("word list2");
        return Arrays.asList(wordList, wordList1);
    }

    public WordList createWordList(String title) {
        System.out.println("list " + title + " created");
        WordList wordList = new WordList();
        wordList.setTitle(title);
        return wordList;
    }

    public void renameList(WordList wordList, String newTitle) {
        wordList.setTitle(newTitle);
    }

    public void archiveList(WordList wordList) {

    }
}
