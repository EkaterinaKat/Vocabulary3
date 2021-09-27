package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.service.*;
import com.katyshevtseva.vocabulary.database.VocDaoImpl;

public class Core {
    private static final Core INSTANCE = new Core();
    private final VocDao vocDao = new VocDaoImpl();
    private final CatalogueService catalogueService = new CatalogueService(vocDao);
    private final SearchService searchService = new SearchService(vocDao);
    private final ListService listService = new ListService(vocDao);
    private final EntryLifecycleService entryLifecycleService = new EntryLifecycleService();
    private final LearningService learningService = new LearningService(vocDao);
    private final FrequentWordService frequentWordService = new FrequentWordService(vocDao);

    public static Core getInstance() {
        return INSTANCE;
    }

    public CatalogueService catalogueService() {
        return catalogueService;
    }

    public SearchService searchService() {
        return searchService;
    }

    public ListService listService() {
        return listService;
    }

    public EntryLifecycleService entryLifecycleService() {
        return entryLifecycleService;
    }

    public LearningService learningService() {
        return learningService;
    }

    public FrequentWordService frequentWordService() {
        return frequentWordService;
    }
}
