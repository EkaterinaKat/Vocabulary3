package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CatalogueService {

    public static List<WordList> getCatalogue(ListStatus status) {
        return VocDao.findListsByArchived(status.listIsArchived);
    }

    public static enum ListStatus {
        ACTIVE(false), ARCHIVED(true);

        private final boolean listIsArchived;

        ListStatus(boolean listIsArchived) {
            this.listIsArchived = listIsArchived;
        }
    }

    public static WordList createWordList(String title) {
        WordList wordList = new WordList();
        wordList.setTitle(title);
        wordList.setArchived(false);
        VocDao.saveNew(wordList);
        return wordList;
    }

    public static void renameList(WordList wordList, String newTitle) {
        wordList.setTitle(newTitle);
        VocDao.saveEdited(wordList);
    }

    public static void archiveList(WordList wordList) {
        wordList.setArchived(true);
        VocDao.saveEdited(wordList);
    }

    public static List<String> getStatistics() {
        List<String> result = new ArrayList<>();
        int totalCount = 0;
        for (int i = 0; i < CoreConstants.MAX_LEVEL; i++) {
            int count = VocDao.countWordsInNotArchivedListsByLevel(i);
            String s = "Level " + i + ": " + count;
            result.add(s);
            totalCount += count;
        }
        result.add("Total: " + totalCount);
        return result;
    }
}
