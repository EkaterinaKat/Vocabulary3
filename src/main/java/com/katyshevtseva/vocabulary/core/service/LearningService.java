package com.katyshevtseva.vocabulary.core.service;

import com.katyshevtseva.vocabulary.core.entity.Entry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.vocabulary.core.CoreConstants.CRITICAL_LEVEL;
import static com.katyshevtseva.vocabulary.core.CoreConstants.MAX_LEVEL;
import static com.katyshevtseva.vocabulary.core.service.EntryLifecycleService.entryIsRipe;

public class LearningService {

    public List<Entry> getEntriesForLearning() {
        List<Entry> allEntries = new ArrayList<>(); //todo достаем записи из бд тип
        List<Entry> entriesToLearn = new ArrayList<>();
        for (Entry entry : allEntries) {
            if (entry.getLevel() < MAX_LEVEL && entryIsRipe(entry))
                entriesToLearn.add(entry);
        }
        entriesToLearn.sort(Comparator.comparing(Entry::getLevel).thenComparing(Entry::getWordList));
        return entriesToLearn;
    }

    public void changeEntryLevel(Entry entry, boolean positiveAnswer) {
        entry.setLastRepeat(new Date());

        if (positiveAnswer) {
            entry.setLevel(entry.getLevel() + 1);
        } else if (entry.getLevel() == 0 || entry.getLevel() >= CRITICAL_LEVEL) {
            entry.setLevel(0);
        } else {
            entry.setLevel(entry.getLevel() - 1);
        }

        //todo сохраняем
    }
}
