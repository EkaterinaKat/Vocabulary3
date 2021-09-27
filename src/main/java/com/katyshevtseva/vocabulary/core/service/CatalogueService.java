package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CatalogueService {
    private final VocDao dao;

    public List<WordList> getCatalogue() {
        return dao.getAllWordLists().stream().filter(wordList -> !wordList.isArchived()).collect(Collectors.toList());
    }

    public WordList createWordList(String title) {
        WordList wordList = new WordList();
        wordList.setTitle(title);
        wordList.setArchived(false);
        dao.saveNew(wordList);
        return wordList;
    }

    public void renameList(WordList wordList, String newTitle) {
        wordList.setTitle(newTitle);
        dao.saveEdited(wordList);
    }

    public void archiveList(WordList wordList) {
        wordList.setArchived(true);
        dao.saveEdited(wordList);
    }
}
