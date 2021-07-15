package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    private VocDao dao;

    public SearchService(VocDao dao) {
        this.dao = dao;
    }

    public List<Entry> search(String string) {
        return dao.getAllEntries().stream()
                .filter(entry -> (entry.getWord().startsWith(string) || entry.getTranslation().startsWith(string)))
                .collect(Collectors.toList());
    }

    public List<FrequentWord> searchFrequentWords(String string) {
        if (string.equals(""))
            return new ArrayList<>();
        return dao.searchFrequentWord(string);
    }
}
