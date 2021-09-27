package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SearchService {
    private final VocDao dao;

    public List<Entry> search(String string) {
        if (string.equals(""))
            return new ArrayList<>();
        return dao.searchEntries(string);
    }

    public List<FrequentWord> searchFrequentWords(String string) {
        if (string.equals(""))
            return new ArrayList<>();
        return dao.searchFrequentWord(string);
    }
}
