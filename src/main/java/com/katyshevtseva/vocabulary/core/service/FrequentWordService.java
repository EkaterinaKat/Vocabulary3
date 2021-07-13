package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;

public class FrequentWordService {
    private VocDao dao;

    public FrequentWordService(VocDao dao) {
        this.dao = dao;
    }

    public int getStatusCount(Status status) {
        return dao.countFrequentWordByStatus(status);
    }

}
