package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.ArrayList;
import java.util.List;

public class CatalogueService {
    private List<WordList> catalogue = new ArrayList<>();

    {
        WordList wordList = new WordList();
        wordList.setTitle("list title1");
        WordList wordList1 = new WordList();
        wordList1.setTitle("word list2");
        catalogue.add(wordList);
        catalogue.add(wordList1);
    }

    public List<WordList> getCatalogue() {
        return catalogue;
    }

    public WordList createWordList(String title) {
        System.out.println("list " + title + " created");
        WordList wordList = new WordList();
        wordList.setTitle(title);
        catalogue.add(wordList);
        return wordList;
    }

    public void renameList(WordList wordList, String newTitle) {
        wordList.setTitle(newTitle);
    }

    public void archiveList(WordList wordList) {
        catalogue.remove(wordList);
    }
}
