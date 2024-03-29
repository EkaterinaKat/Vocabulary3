package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CatalogueService {
    private final VocDao dao;

    public List<WordList> getCatalogue(ListStatus status) {
        return dao.findListsByArchived(status.listIsArchived);
    }

    public static enum ListStatus {
        ACTIVE(false), ARCHIVED(true);

        private final boolean listIsArchived;

        ListStatus(boolean listIsArchived) {
            this.listIsArchived = listIsArchived;
        }
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

    public List<String> getStatistics() {
        List<String> result = new ArrayList<>();
        int totalCount = 0;
        for (int i = 0; i < CoreConstants.MAX_LEVEL; i++) {
            int count = dao.countWordsInNotArchivedListsByLevel(i);
            String s = "Level " + i + ": " + count;
            result.add(s);
            totalCount += count;
        }
        result.add("Total: " + totalCount);
        return result;
    }
}
