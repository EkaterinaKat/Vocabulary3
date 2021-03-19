package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private VocDao dao;

    public SearchService(VocDao dao) {
        this.dao = dao;
    }

    public List<Entry> search(String string) {
        List<Entry> entries = dao.getAllEntries();
        List<Entry> result = new ArrayList<>();
        for (Entry entry : entries)
            if (entry.getWord().startsWith(string) || entry.getTranslation().startsWith(string))
                result.add(entry);
        return result;
    }
}
