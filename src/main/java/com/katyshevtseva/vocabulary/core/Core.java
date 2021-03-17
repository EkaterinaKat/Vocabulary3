package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.service.*;

public class Core {
    private static Core INSTANCE = new Core();
    private CatalogueService catalogueService = new CatalogueService();
    private SearchService searchService = new SearchService();
    private ListService listService = new ListService();
    private EntryLifecycleService entryLifecycleService = new EntryLifecycleService();
    private LearningService learningService = new LearningService();

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

}
