package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.WordList;

import java.util.ArrayList;
import java.util.List;

public class CatalogueService {
    private VocDao dao;

    public CatalogueService(VocDao dao) {
        this.dao = dao;
    }

    public List<WordList> getCatalogue() {
        List<WordList> allLists = dao.getAllWordLists();
        List<WordList> notArchivedLists = new ArrayList<>();
        for (WordList wordList : allLists)
            if (!wordList.isArchived())
                notArchivedLists.add(wordList);
        return notArchivedLists;
    }

    public WordList createWordList(String title) {
        WordList wordList = new WordList();
        wordList.setTitle(title);
        wordList.setArchived(false);
        dao.saveNewWordList(wordList);
        return wordList;
    }

    public void renameList(WordList wordList, String newTitle) {
        wordList.setTitle(newTitle);
        dao.saveEditedWordList(wordList);
    }

    public void archiveList(WordList wordList) {
        wordList.setArchived(true);
        dao.saveEditedWordList(wordList);
    }
}
