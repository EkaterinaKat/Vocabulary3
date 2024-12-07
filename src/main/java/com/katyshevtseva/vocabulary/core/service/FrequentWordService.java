package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.general.PieChartData;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;
import com.katyshevtseva.vocabulary.database.VocDao;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

public class FrequentWordService {

    public static PieChartData getStatusCountPieChartData() {
        PieChartData data = new PieChartData();

        for (Status status : Status.values()) {
            data.addSegment(new PieChartData.Segment(getStatusCount(status), status.toString()));
        }

        return data;
    }

    private static int getStatusCount(Status status) {
        return VocDao.countFrequentWordByStatus(status);
    }

    public static List<FrequentWord> getWordsForSorting() {
        List<FrequentWord> words = VocDao.getFrequentWordsByStatus(Status.INTACT);
        Collections.shuffle(words);
        return words;
    }

    public static void sort(FrequentWord frequentWord, boolean positiveAnswer) {
        frequentWord.setStatus(positiveAnswer ? Status.KNOWN : Status.NEED_TO_LEARN);
        VocDao.saveEdited(frequentWord);
    }

}
