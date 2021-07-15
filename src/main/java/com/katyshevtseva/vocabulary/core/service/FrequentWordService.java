package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.general.PieChartData;
import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;

import java.util.Collections;
import java.util.List;

public class FrequentWordService {
    private VocDao dao;

    public FrequentWordService(VocDao dao) {
        this.dao = dao;
    }

    public PieChartData getStatusCountPieChartData() {
        PieChartData data = new PieChartData();

        for (Status status : Status.values()) {
            data.addSegment(new PieChartData.Segment(getStatusCount(status), status.toString()));
        }

        return data;
    }

    private int getStatusCount(Status status) {
        return dao.countFrequentWordByStatus(status);
    }

    public List<FrequentWord> getAllIntactWords() {
        List<FrequentWord> words = dao.getFrequentWordsByStatus(Status.INTACT);
        Collections.shuffle(words);
        return words;
    }

    public void sort(FrequentWord frequentWord, boolean positiveAnswer) {
        frequentWord.setStatus(positiveAnswer ? Status.KNOWN : Status.NEED_TO_LEARN);
        dao.saveEditedFrequentWord(frequentWord);
    }

}
