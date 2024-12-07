package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public static List<Entry> search(String string) {
        if (string.equals(""))
            return new ArrayList<>();
        return VocDao.searchEntries(string);
    }

    public static List<FrequentWord> searchFrequentWords(String string) {
        if (string.equals(""))
            return new ArrayList<>();
        return VocDao.searchFrequentWord(string);
    }
}
